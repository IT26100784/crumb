package com.crumb.order.service;

import com.crumb.order.dto.OrderCreateRequest;
import com.crumb.order.dto.OrderResponse;
import com.crumb.order.dto.OrderStatusUpdateRequest;
import com.crumb.order.entity.Order;

import java.util.List;

//Business contract for retail order placement, tracking, and management.
public interface OrderService {

    OrderResponse placeOrder(Long customerId, OrderCreateRequest request);

    List<OrderResponse> getCustomerOrders(Long customerId);

    List<OrderResponse> getAllOrders(String status, String orderType);

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByNumber(String orderNumber);

    Order getOrderEntity(Long id);

    OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request);

    //Cancels an order (by customer before preparation or by admin).
    OrderResponse cancelOrder(Long id, Long customerId, boolean isAdmin);

    //Deletes an order record (admin only).
    void deleteOrder(Long id);

    double getTotalRevenue();
}
