package com.appsdeveloper.estore.OrdersService.query;

import com.appsdeveloper.estore.OrdersService.command.commands.CreateOrderCommand;
import com.appsdeveloper.estore.OrdersService.command.rest.CreateOrderRestModel;
import com.appsdeveloper.estore.OrdersService.core.data.OrderEntity;
import com.appsdeveloper.estore.OrdersService.core.model.OrderSummary;
import com.appsdeveloper.estore.OrdersService.query.rest.GetAllOrdersQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import com.appsdeveloper.estore.OrdersService.core.data.OrdersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Component
public class   OrdersQueryHandler {

    OrdersRepository ordersRepository;

    public OrdersQueryHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(),orderEntity.getProductId(),
                orderEntity.getOrderStatus(), "");
    }

    @QueryHandler
    public List<OrderEntity> findOrderById(FindOrderByIdQuery findOrderByIdQuery) {
        List<OrderEntity> orderEntities = ordersRepository.findByUserId(findOrderByIdQuery.getUserId());
        return orderEntities;
    }

    @QueryHandler
    public List<OrderEntity> getAllOrders(GetAllOrdersQuery getAllOrdersQuery) {
        List<OrderEntity> orderEntities = ordersRepository.findAll();
        return orderEntities;
    }
}
