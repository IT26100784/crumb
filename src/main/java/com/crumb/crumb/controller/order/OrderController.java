package com.crumb.crumb.controller.order;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.repository.order.OrderRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public OrderModel addOrder(@RequestBody OrderModel order) {
        return orderRepository.save(order);
    }
}