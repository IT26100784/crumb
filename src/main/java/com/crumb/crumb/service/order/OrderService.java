package com.crumb.crumb.service.order;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import com.crumb.crumb.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Save a new order. New orders always start as PENDING.
    public OrderModel createOrder(OrderModel order) {
        order.setId(null);
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderModel getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    public List<OrderModel> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<OrderModel> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}