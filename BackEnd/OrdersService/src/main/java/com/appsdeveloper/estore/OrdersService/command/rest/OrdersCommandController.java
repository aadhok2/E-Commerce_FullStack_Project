package com.appsdeveloper.estore.OrdersService.command.rest;

import com.appsdeveloper.estore.Core.model.User;
import com.appsdeveloper.estore.OrdersService.command.commands.CreateOrderCommand;
import com.appsdeveloper.estore.OrdersService.core.data.OrderEntity;
import com.appsdeveloper.estore.OrdersService.core.data.OrdersRepository;
import com.appsdeveloper.estore.OrdersService.core.model.OrderStatus;
import com.appsdeveloper.estore.OrdersService.core.model.OrderSummary;
import com.appsdeveloper.estore.OrdersService.query.FindOrderQuery;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.weaver.ast.Or;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8082"},
        methods = {RequestMethod.GET, RequestMethod.OPTIONS , RequestMethod.POST}
        ,allowCredentials = "true")
public class OrdersCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    private OrdersRepository ordersRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(OrdersCommandController.class);

    @Autowired
    public OrdersCommandController(CommandGateway commandGateway, QueryGateway queryGateway,
                                   OrdersRepository ordersRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.ordersRepository = ordersRepository;
    }

    @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8082"}, allowCredentials = "true")
    @PostMapping("/create")
    public OrderSummary createOrder(@Valid @RequestBody CreateOrderRestModel order) {
        try {
            String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
            String orderId = UUID.randomUUID().toString();
            CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                    .addressId(order.getAddressId())
                    .productId(order.getProductId())
                    .userId(order.getUserId())
                    .quantity(order.getQuantity())
                    .orderId(orderId)
                    .orderStatus(OrderStatus.CREATED)
                    .build();

            SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult =
                    queryGateway.subscriptionQuery(new FindOrderQuery(orderId),
                            ResponseTypes.instanceOf(OrderSummary.class),
                            ResponseTypes.instanceOf(OrderSummary.class));
            try {
                commandGateway.sendAndWait(createOrderCommand);
                return queryResult.updates().blockFirst();
            } finally {
                queryResult.close();
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            return null;
        }
    }
}