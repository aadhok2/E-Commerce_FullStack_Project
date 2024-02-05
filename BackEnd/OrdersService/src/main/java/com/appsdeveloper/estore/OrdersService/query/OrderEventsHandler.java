package com.appsdeveloper.estore.OrdersService.query;

import com.appsdeveloper.estore.OrdersService.core.data.OrderEntity;
import com.appsdeveloper.estore.OrdersService.core.data.OrdersRepository;
import com.appsdeveloper.estore.OrdersService.core.events.OrderApprovedEvent;
import com.appsdeveloper.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloper.estore.OrdersService.core.data.OrdersRepository;
import com.appsdeveloper.estore.OrdersService.core.events.OrderRejectedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class  OrderEventsHandler {

    private final OrdersRepository ordersRepository;

    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

//    @ExceptionHandler(resultType = IllegalArgumentException.class)
//    public void handle(IllegalArgumentException ex){
//        //Log error message
//    }
//
//    @ExceptionHandler(resultType = Exception.class)
//    public void handle(Exception ex) throws Exception {
//        //throw error message
//        throw ex;
//    }

    @EventHandler
    public void on(OrderCreatedEvent OrderCreatedEvent){

        OrderEntity OrderEntity = new OrderEntity();
        BeanUtils.copyProperties(OrderCreatedEvent, OrderEntity);

        try{
            ordersRepository.save(OrderEntity);
        }
        catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }
    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());

        if(orderEntity == null) {
            // TODO: Do something about it
            return;
        }

        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());

        ordersRepository.save(orderEntity);

    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
        orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
}

