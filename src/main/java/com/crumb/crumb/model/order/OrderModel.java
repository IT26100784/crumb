package com.crumb.crumb.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @NotNull(message = "Required date is needed")
    @FutureOrPresent(message = "Required date cannot be in the past")
    private LocalDate requiredDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    @Size(max = 255, message = "Note must be 255 characters or less")
    private String specialNote;

    public OrderModel(Long customerId, LocalDate requiredDate,
                      BigDecimal totalAmount, String specialNote) {
        this.customerId = customerId;
        this.requiredDate = requiredDate;
        this.totalAmount = totalAmount;
        this.specialNote = specialNote;
        this.status = OrderStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void startBaking() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED orders can start baking");
        }
        this.status = OrderStatus.IN_PROGRESS;
    }

    public void markReady() {
        if (status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only IN_PROGRESS orders can be marked ready");
        }
        this.status = OrderStatus.READY;
    }

    public void complete() {
        if (status != OrderStatus.READY) {
            throw new IllegalStateException("Only READY orders can be completed");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (!isCancellable()) {
            throw new IllegalStateException("This order can no longer be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public boolean isCancellable() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public String getOrderCode() {
        if (id == null) {
            return null;
        }
        return String.format("O%03d", id);
    }

}
public class OrderModel {

}
