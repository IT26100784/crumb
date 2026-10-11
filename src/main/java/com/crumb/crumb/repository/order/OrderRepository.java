package com.crumb.crumb.repository.order;

import com.crumb.crumb.model.order.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {
}