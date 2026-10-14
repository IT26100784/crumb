package com.crumb.crumb.service.order;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import com.crumb.crumb.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public OrderModel confirmOrder(Long id) {
        OrderModel order = getOrderById(id);
        order.confirm();
        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel startBaking(Long id) {
        OrderModel order = getOrderById(id);
        order.startBaking();
        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel markOrderReady(Long id) {
        OrderModel order = getOrderById(id);
        order.markReady();
        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel completeOrder(Long id) {
        OrderModel order = getOrderById(id);
        order.complete();
        return orderRepository.save(order);
    }

    @Transactional
    public OrderModel cancelOrder(Long id) {
        OrderModel order = getOrderById(id);
        order.cancel();
        return orderRepository.save(order);
    }

}