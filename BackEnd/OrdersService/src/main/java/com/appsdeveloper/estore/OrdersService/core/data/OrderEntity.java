package com.appsdeveloper.estore.OrdersService.core.data;


import com.appsdeveloper.estore.OrdersService.core.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="orders")
public class OrderEntity implements Serializable {
    @Id
    @Column(unique = true)
    private String orderId;
    private String productId;
    private String userId;

    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
