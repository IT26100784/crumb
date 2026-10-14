package com.crumb.crumb.dto;

import com.crumb.crumb.model.order.OrderModel;
import com.crumb.crumb.model.order.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

    private final Long id;
    private final String customerId;
    private final LocalDateTime orderDate;
    private final LocalDate requiredDate;
    private final OrderStatus status;
    private final BigDecimal totalAmount;
    private final String specialNote;
    private final String orderCode;
    private final boolean cancellable;

    public static OrderResponse from(OrderModel order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getRequiredDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getSpecialNote(),
                order.getOrderCode(),
                order.isCancellable()
        );
    }
}