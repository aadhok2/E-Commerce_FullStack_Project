package com.appsdeveloper.estore.ProductsService.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRestModel {

    @NotBlank(message = "Product title cannot be empty")
    private String title;

    @NotBlank(message = "Product title cannot be empty")
    private String galleryName;

    @Min(value=1,message = "Price cannot be less than 1")
    private BigDecimal price;

    @Min(value=1,message = "Quantity cannot be lower than 1")
    @Max(value=10,message = "Quantity cannot be larger than 10")
    private int quantity;

    private Data createdOn;
    private String description;


}
