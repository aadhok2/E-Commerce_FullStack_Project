package com.appsdeveloper.estore.Core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Builder
@Data
public class ReserveProductCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;
    private final int quantity;
    private final String userId;

}
