package com.crumb.crumb.controller.order;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import com.crumb.crumb.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel createOrder(@Valid @RequestBody OrderModel order) {
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<OrderModel> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderModel getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderModel> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/status/{status}")
    public List<OrderModel> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    // PUT /api/orders/1/confirm
    @PutMapping("/{id}/confirm")
    public OrderModel confirmOrder(@PathVariable Long id) {
        return orderService.confirmOrder(id);
    }

    // PUT /api/orders/1/start-baking
    @PutMapping("/{id}/start-baking")
    public OrderModel startBaking(@PathVariable Long id) {
        return orderService.startBaking(id);
    }

    // PUT /api/orders/1/ready
    @PutMapping("/{id}/ready")
    public OrderModel markOrderReady(@PathVariable Long id) {
        return orderService.markOrderReady(id);
    }

    // PUT /api/orders/1/complete
    @PutMapping("/{id}/complete")
    public OrderModel completeOrder(@PathVariable Long id) {
        return orderService.completeOrder(id);
    }

    // PUT /api/orders/1/cancel
    @PutMapping("/{id}/cancel")
    public OrderModel cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

}