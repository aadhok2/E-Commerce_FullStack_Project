package com.appsdeveloper.estore.OrdersService.saga;

import com.appsdeveloper.estore.Core.commands.CancelProductReservationCommand;
import com.appsdeveloper.estore.Core.commands.ProcessPaymentCommand;
import com.appsdeveloper.estore.Core.commands.ReserveProductCommand;
import com.appsdeveloper.estore.Core.events.PaymentProcessedEvent;
import com.appsdeveloper.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloper.estore.Core.events.ProductReservedEvent;
import com.appsdeveloper.estore.Core.model.User;
import com.appsdeveloper.estore.Core.query.FetchUserPaymentDetailsQuery;
import com.appsdeveloper.estore.OrdersService.command.commands.RejectOrderCommand;
import com.appsdeveloper.estore.OrdersService.command.commands.ApproveOrderCommand;
import com.appsdeveloper.estore.OrdersService.core.events.OrderApprovedEvent;
import com.appsdeveloper.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloper.estore.OrdersService.core.events.OrderRejectedEvent;
import com.appsdeveloper.estore.OrdersService.core.model.OrderSummary;
import com.appsdeveloper.estore.OrdersService.query.FindOrderQuery;
import jakarta.transaction.TransactionManager;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga  {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @Autowired
    private transient DeadlineManager deadlineManager ;

    private String scheduleId = null;

    private final Logger LOGGER =LoggerFactory.getLogger(OrderSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){

        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .productId(orderCreatedEvent.getProductId())
                .orderId(orderCreatedEvent.getOrderId())
                .userId(orderCreatedEvent.getUserId())
                .quantity(orderCreatedEvent.getQuantity())
                .build();
        LOGGER.info("Order created event handled for orderId: " + reserveProductCommand.getOrderId()
        +" and productId: "+ reserveProductCommand.getProductId());
        try {
            commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

                @Override
                public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
                                     CommandResultMessage<? extends Object> commandResultMessage) {
                    if(commandResultMessage.isExceptional()) {
                        // Start a compensating transaction
                        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(),
                                orderCreatedEvent.getProductId(),
                                commandResultMessage.exceptionResult().getMessage());

                        commandGateway.send(rejectOrderCommand);
                    }

                }

            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent)
    {
        //process user payment
        LOGGER.info("Product reserved event called for orderId: " + productReservedEvent.getOrderId()
                +" and productId: "+ productReservedEvent.getProductId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery =
                new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());

        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }
        catch (Exception ex)
        {
            LOGGER.info(ex.getMessage());
            //Start compensating transaction
            cancelProductReservation(productReservedEvent, "Cancelled product reservation for reason "+
                    ex.getMessage());
            return;
        }
        if(userPaymentDetails == null)
        {
            //Start compensating transaction
            cancelProductReservation(productReservedEvent, "Could not fetch user payment deatils");
            return;
        }
        LOGGER.error("Successfully Fetched user payment details for user: " + userPaymentDetails.getFirstName());

        scheduleId = deadlineManager.schedule(Duration.of(1, ChronoUnit.SECONDS),
                "payment-processing-deadline",productReservedEvent);

//        if(true) return;
        //Send process payment command
        ProcessPaymentCommand processPaymentCommand =
                ProcessPaymentCommand.builder()
                        .orderId(productReservedEvent.getOrderId())
                        .paymentDetails(userPaymentDetails.getPaymentDetails())
                        .paymentId(UUID.randomUUID().toString())
                        .build();
        String result = null;

        try{
            result = commandGateway.sendAndWait(processPaymentCommand);
        }
        catch (Exception ex){
            LOGGER.error(ex.getMessage());
            //Start compensating transaction
            cancelProductReservation(productReservedEvent, "Cancelled product reservation for reason "+
                    ex.getMessage());
            return;
        }
        if(result==null){
            LOGGER.info("The ProcessPaymentCommand resulted in null starting a compensating transaction...");
            //Start compensating transaction
            cancelProductReservation(productReservedEvent,"Could not process user payment with provided payment details");
        }

    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent,
                                          String reason)
    {
        cancelDeadLine();

        CancelProductReservationCommand cancelProductReservationCommand =
                CancelProductReservationCommand.builder()
                        .productId(productReservedEvent.getProductId())
                        .orderId(productReservedEvent.getOrderId())
                        .userId(productReservedEvent.getUserId())
                        .quantity(productReservedEvent.getQuantity())
                        .reason(reason)
                        .build();
        commandGateway.send(cancelProductReservationCommand);
    }


    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent)
    {
        cancelDeadLine();
        //Send an ApproveOrderCommand
        ApproveOrderCommand approveOrderCommand =
                new ApproveOrderCommand(paymentProcessedEvent.getOrderId(),
                        paymentProcessedEvent.getProductId());
        commandGateway.send(approveOrderCommand);
    }

    private void cancelDeadLine(){
        if(scheduleId!=null)
            deadlineManager.cancelSchedule("payment-processing-deadline",scheduleId);
        scheduleId=null;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent)
    {
        LOGGER.info("Order is approved for orderId: "+orderApprovedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class,query->true,
                new OrderSummary(orderApprovedEvent.getOrderId(),
                        orderApprovedEvent.getProductId()
                        ,orderApprovedEvent.getOrderStatus(),""));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent){
        //Create and send RejectOrderCommand
        RejectOrderCommand rejectOrderCommand =
                new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),
                        productReservationCancelledEvent.getProductId(),
                        productReservationCancelledEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent){
        LOGGER.info("Successfully rejected order with id: "+orderRejectedEvent.getOrderId()
        +" and reason: "+ orderRejectedEvent.getReason());
        queryUpdateEmitter.emit(FindOrderQuery.class,query->true,
                new OrderSummary(orderRejectedEvent.getOrderId()
                        ,orderRejectedEvent.getProductId()
                        ,orderRejectedEvent.getOrderStatus()
                        ,orderRejectedEvent.getReason()));
    }

    @DeadlineHandler(deadlineName = "payment-processing-deadline")
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent){
        LOGGER.info("Payemnt processing deadline took place.Sending a compensating message to cancel reservation");
        cancelProductReservation(productReservedEvent,"Payment Timeout ");

    }


}
