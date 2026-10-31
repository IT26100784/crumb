package com.crumb.customer.dto;

import com.crumb.customer.entity.Customer;
import com.crumb.customer.entity.PremiumCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String customerType;
    private String badge;
    private Integer loyaltyPoints;
    private int maxActiveBookings;
    private double deliveryDiscount;
    private LocalDate membershipExpiry;
    private boolean active;
    private LocalDateTime createdAt;

    public static CustomerResponse fromEntity(Customer customer) {
        if (customer == null) return null;

        LocalDate expiry = null;
        if (customer instanceof PremiumCustomer premium) {
            expiry = premium.getMembershipExpiry();
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .role(customer.getRole())
                .customerType(customer instanceof PremiumCustomer ? "PREMIUM" : "REGULAR")
                .badge(customer.getCustomerBadge())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .maxActiveBookings(customer.getMaxActiveBookings())
                .deliveryDiscount(customer.getDeliveryFeeDiscount())
                .membershipExpiry(expiry)
                .active(customer.isActive())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}