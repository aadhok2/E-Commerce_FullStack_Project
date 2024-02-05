package com.appsdeveloper.estore.OrdersService.command;

import com.appsdeveloper.estore.OrdersService.command.commands.ApproveOrderCommand;
import com.appsdeveloper.estore.OrdersService.command.commands.CreateOrderCommand;
import com.appsdeveloper.estore.OrdersService.command.commands.RejectOrderCommand;
import com.appsdeveloper.estore.OrdersService.core.events.OrderApprovedEvent;
import com.appsdeveloper.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloper.estore.OrdersService.core.events.OrderRejectedEvent;
import com.appsdeveloper.estore.OrdersService.core.model.OrderStatus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
    private final Logger LOGGER = LoggerFactory.getLogger(OrderAggregate.class);

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        try {
            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
            BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);

            AggregateLifecycle.apply(orderCreatedEvent);
        }
        catch(Exception ex){
            LOGGER.info(ex.getMessage());
        }
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
        LOGGER.info("Successfully created order for product: "+orderCreatedEvent.getProductId() + " at "
                + LocalDateTime.now());
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand) {
        // Create and publish the OrderApprovedEvent

        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId(),
                approveOrderCommand.getProductId());

        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {

        this.orderStatus = orderApprovedEvent.getOrderStatus();
        LOGGER.info("Approved order for order id: "+orderApprovedEvent.getOrderId() + " at "
                + LocalDateTime.now());
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {

        OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(),
                rejectOrderCommand.getProductId(),
                rejectOrderCommand.getReason());

        AggregateLifecycle.apply(orderRejectedEvent);

    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent)   {
        this.orderStatus = orderRejectedEvent.getOrderStatus();
        LOGGER.info("Rejected order for order id: "+orderRejectedEvent.getOrderId() + " at "
                + LocalDateTime.now());
    }


}