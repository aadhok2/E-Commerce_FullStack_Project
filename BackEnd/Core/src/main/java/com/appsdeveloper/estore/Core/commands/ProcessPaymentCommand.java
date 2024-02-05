package com.appsdeveloper.estore.Core.commands;

import com.appsdeveloper.estore.Core.model.PaymentDetails;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class ProcessPaymentCommand {

    @TargetAggregateIdentifier
    private final String paymentId;
    private final String orderId;
    private final String productId;
    private final PaymentDetails paymentDetails;
}
