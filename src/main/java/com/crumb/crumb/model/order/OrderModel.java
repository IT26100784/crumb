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

}