package com.crumb.crumb.controller.order;

import com.crumb.crumb.dto.CreateOrderRequest;
import com.crumb.crumb.dto.OrderResponse;
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
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderModel order = new OrderModel(
                request.getCustomerId(),
                request.getRequiredDate(),
                request.getTotalAmount(),
                request.getSpecialNote()
        );

        return OrderResponse.from(orderService.createOrder(order));
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return OrderResponse.from(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public OrderResponse updateOrder(@PathVariable Long id,
                                     @Valid @RequestBody CreateOrderRequest request) {
        return OrderResponse.from(orderService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getOrdersByCustomer(@PathVariable String customerId) {
        return orderService.getOrdersByCustomer(customerId)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @GetMapping("/status/{status}")
    public List<OrderResponse> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @PutMapping("/{id}/confirm")
    public OrderResponse confirmOrder(@PathVariable Long id) {
        return OrderResponse.from(orderService.confirmOrder(id));
    }

    @PutMapping("/{id}/start-baking")
    public OrderResponse startBaking(@PathVariable Long id) {
        return OrderResponse.from(orderService.startBaking(id));
    }

    @PutMapping("/{id}/ready")
    public OrderResponse markOrderReady(@PathVariable Long id) {
        return OrderResponse.from(orderService.markOrderReady(id));
    }

    @PutMapping("/{id}/complete")
    public OrderResponse completeOrder(@PathVariable Long id) {
        return OrderResponse.from(orderService.completeOrder(id));
    }

    @PutMapping("/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long id) {
        return OrderResponse.from(orderService.cancelOrder(id));
    }
}
