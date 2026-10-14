package com.crumb.crumb.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "Customer id is required")
    @Size(max = 50, message = "Customer id is too long")
    private String customerId;

    @NotNull(message = "Required date is needed")
    @FutureOrPresent(message = "Required date cannot be in the past")
    private LocalDate requiredDate;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than zero")
    private BigDecimal totalAmount;

    @Size(max = 255, message = "Note must be 255 characters or less")
    private String specialNote;
}