package com.appsdeveloper.estore.OrdersService.core.model;

import lombok.Value;


@Value
public class OrderSummary {

    private final String orderId;
    private final String productId;
    private final OrderStatus orderStatus;
    private final String message;

}