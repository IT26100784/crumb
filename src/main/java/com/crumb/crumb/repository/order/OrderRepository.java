package com.crumb.crumb.repository.order;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    // Find all orders placed by one customer
    List<OrderModel> findByCustomerId(String customerId);

    // Find all orders with a given status (e.g. all PENDING orders)
    List<OrderModel> findByStatus(OrderStatus status);
}