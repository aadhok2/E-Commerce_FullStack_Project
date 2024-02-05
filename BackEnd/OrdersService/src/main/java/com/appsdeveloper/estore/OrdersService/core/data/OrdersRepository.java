package com.appsdeveloper.estore.OrdersService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrderEntity,String> {

    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findByUserId(String userId);
}
