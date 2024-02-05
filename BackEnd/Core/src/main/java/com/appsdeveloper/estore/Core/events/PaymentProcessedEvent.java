package com.appsdeveloper.estore.Core.events;


import lombok.Builder;
import lombok.Data;

@Data
public class PaymentProcessedEvent {
    private final String orderId;
    private final String productId;
    private final String paymentId;
}
