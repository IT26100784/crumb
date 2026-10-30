package com.crumb.payment.service;

import com.crumb.payment.dto.PaymentRequest;

import java.util.List;

/**
 * PaymentService - Business contract for processing payments and managing receipts.
 *
 * // ABSTRACTION:
 * Defines business specifications for transaction settlement and refund operations.
 */
// ABSTRACTION
public interface PaymentService {

    /**
     * Processes a payment for an order or custom cake commission.
     */
    com.crumb.payment.dto.PaymentResponse processPayment(Long customerId, PaymentRequest request);

    /**
     * Retrieves all payments processed for a customer.
     */
    List<com.crumb.payment.dto.PaymentResponse> getCustomerPayments(Long customerId);

    /**
     * Admin endpoint: Lists all payments with optional status and method filters.
     */
    List<com.crumb.payment.dto.PaymentResponse> getAllPayments(String status, String paymentType);

    /**
     * Retrieves payment by primary key.
     */
    com.crumb.payment.dto.PaymentResponse getPaymentById(Long id);

    /**
     * Retrieves payment by public transaction reference code.
     */
    com.crumb.payment.dto.PaymentResponse getPaymentByReference(String reference);

    /**
     * Admin refund endpoint.
     */
    com.crumb.payment.dto.PaymentResponse refundPayment(Long id);

    /**
     * Total processed payment volume.
     */
    double getTotalCollectedRevenue();
}

