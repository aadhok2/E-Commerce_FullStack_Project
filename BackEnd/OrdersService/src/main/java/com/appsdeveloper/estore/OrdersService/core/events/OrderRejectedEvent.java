package com.appsdeveloper.estore.OrdersService.core.events;


import com.appsdeveloper.estore.OrdersService.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String productId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}