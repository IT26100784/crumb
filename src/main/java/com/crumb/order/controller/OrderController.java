package com.crumb.order.controller;

import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
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
}