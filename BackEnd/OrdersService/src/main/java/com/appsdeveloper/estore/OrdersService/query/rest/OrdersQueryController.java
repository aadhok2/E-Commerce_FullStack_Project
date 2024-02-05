package com.appsdeveloper.estore.OrdersService.query.rest;

import com.appsdeveloper.estore.OrdersService.core.data.OrderEntity;
import com.appsdeveloper.estore.OrdersService.core.model.OrderSummary;
import com.appsdeveloper.estore.OrdersService.query.FindOrderByIdQuery;
import com.appsdeveloper.estore.OrdersService.query.FindOrderQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8082"},
        methods = {RequestMethod.GET, RequestMethod.OPTIONS , RequestMethod.POST}
        ,allowCredentials = "true")
public class OrdersQueryController {

    @Autowired
    QueryGateway queryGateway;

    private Logger LOGGER = LoggerFactory.getLogger(OrdersQueryController.class);


    @GetMapping("/get/user/{userId}")
    public List<OrderEntity> getOrdersByUser(@PathVariable String userId)
    {
        FindOrderByIdQuery findOrdersByUserIdQuery = new FindOrderByIdQuery(userId);
        List<OrderEntity> orderEntities = queryGateway.query(findOrdersByUserIdQuery,
                ResponseTypes.multipleInstancesOf(OrderEntity.class)).join();
        return orderEntities;

    }


    @GetMapping("/get/{orderId}")
    public OrderSummary getOrder(@PathVariable String orderId)
    {
        FindOrderQuery findOrderQuery = new FindOrderQuery(orderId);
        OrderSummary orderSummary = queryGateway.query(findOrderQuery,
                ResponseTypes.instanceOf(OrderSummary.class)).join();
        return orderSummary;
    }

    @GetMapping("/get")
    public List<OrderEntity> getAllOrders()
    {
        GetAllOrdersQuery getAllOrdersQuery = new GetAllOrdersQuery();
        List<OrderEntity> orderEntities = queryGateway.query(getAllOrdersQuery,
                ResponseTypes.multipleInstancesOf(OrderEntity.class)).join();
        return orderEntities;
    }
}
