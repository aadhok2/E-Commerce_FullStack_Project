package com.appsdeveloper.estore.ProductsService.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private  final String title;
    private final String galleryName;
    private final BigDecimal price;
    private final int quantity;
    private final String imageId;
    private final Date createdOn;
    private final String description;
}
