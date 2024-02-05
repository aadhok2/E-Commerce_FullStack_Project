package com.appsdeveloper.estore.ProductsService.command;

import com.appsdeveloper.estore.Core.commands.CancelProductReservationCommand;
import com.appsdeveloper.estore.Core.commands.ReserveProductCommand;
import com.appsdeveloper.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloper.estore.Core.events.ProductReservedEvent;
import com.appsdeveloper.estore.ProductsService.core.events.ProductCreatedEvent;
import com.appsdeveloper.estore.ProductsService.query.ProductEventsHandler;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Aggregate(snapshotTriggerDefinition="productSnapshotTriggerDefinition")
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private int quantity;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    public ProductAggregate() {

    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws IllegalAccessException {
        //Validate Create Product Command
        LOGGER.info("Received create Product  command for product id: "+createProductCommand.getProductId() + " at "
        + LocalDateTime.now());

        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalAccessException("Price cannot be less than or equal to zero");
        }
        if (createProductCommand.getTitle() == null ||
                createProductCommand.getTitle().isBlank()) {
            throw new IllegalAccessException("Title cannot be Blank");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle (ReserveProductCommand reserveProductCommand)
    {
        LOGGER.info("Received reserve Product command for product id: "+reserveProductCommand.getProductId() + " at "
                + LocalDateTime.now());
        if(this.quantity < reserveProductCommand.getQuantity()) {
            LOGGER.info("Insufficient number of items in stock");
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();
        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand){
        LOGGER.info("Received Cancel Product reservation command for product id: "+
                cancelProductReservationCommand.getProductId() + " at " +LocalDateTime.now());
        ProductReservationCancelledEvent productReservationCancelledEvent =
                ProductReservationCancelledEvent.builder()
                        .productId(cancelProductReservationCommand.getProductId())
                        .orderId(cancelProductReservationCommand.getOrderId())
                        .userId(cancelProductReservationCommand.getUserId())
                        .quantity(cancelProductReservationCommand.getQuantity())
                        .reason(cancelProductReservationCommand.getReason())
                        .build();
        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
        this.title = productCreatedEvent.getTitle();
        this.productId = productCreatedEvent.getProductId();
        LOGGER.info("Successfully created product for product id: "+productCreatedEvent.getProductId() + " at "
                + LocalDateTime.now());
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent)
    {
        this.quantity -= productReservedEvent.getQuantity();
        LOGGER.info("Reserved the product  and adjusted quantity for product id: "+productReservedEvent.getProductId() + " at "
                + LocalDateTime.now());
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent)
    {

        this.quantity += productReservationCancelledEvent.getQuantity();
        LOGGER.info("Cancelled reservation and adjusted quantity for product id : "+productReservationCancelledEvent.getProductId() + " at "
                + LocalDateTime.now());
    }

}
