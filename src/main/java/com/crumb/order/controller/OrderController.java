package com.crumb.order.controller;

import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
import com.crumb.common.exception.ForbiddenException;
import com.crumb.order.dto.OrderCreateRequest;
import com.crumb.order.dto.OrderResponse;
import com.crumb.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//REST API for retail bakery ordering, status timeline, and fulfillment.
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Customer endpoint: Place a new order from cart.
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody OrderCreateRequest request,
            HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        OrderResponse response = orderService.placeOrder(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Order placed successfully! Reference: " + response.getOrderNumber(), response));
    }

    //Customer endpoint: View personal order history.
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        List<OrderResponse> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /*
    Tracking endpoint: View order status timeline by tracking order number.
    Accessible by customers or guests with the receipt code.
    */
    @GetMapping("/track/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> trackOrder(@PathVariable String orderNumber) {
        OrderResponse order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    //Retrieve detailed order by ID.
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        OrderResponse order = orderService.getOrderById(id);
        if (!isAdmin && !order.getCustomerId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to view another customer's order.");
        }

        return ResponseEntity.ok(ApiResponse.success(order));
    }

    //Cancel an order prior to kitchen preparation.
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        OrderResponse cancelled = orderService.cancelOrder(id, currentUserId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Order has been cancelled.", cancelled));
    }
}