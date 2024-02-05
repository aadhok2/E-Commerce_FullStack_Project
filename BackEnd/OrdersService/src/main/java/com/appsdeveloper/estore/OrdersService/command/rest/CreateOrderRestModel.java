package com.appsdeveloper.estore.OrdersService.command.rest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
public class CreateOrderRestModel {


    @NotBlank(message = "Order productId is a required field")
    private String productId;

    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 5, message = "Quantity cannot be larger than 5")
    private int quantity;

    @NotBlank(message = "Order addressId is a required field")
    private String addressId;

    @NotBlank(message = "Order User is a required field")
    private String userId;

}
