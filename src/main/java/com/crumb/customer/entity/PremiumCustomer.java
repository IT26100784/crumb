package com.crumb.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PREMIUM")
@Getter
@Setter
@NoArgsConstructor
public class PremiumCustomer extends Customer {

    @Column(name = "membership_expiry")
    private LocalDate membershipExpiry = LocalDate.now().plusYears(1);

    @Column(name = "vip_concierge_note")
    private String vipConciergeNote = "Priority custom cake consultation";

    public PremiumCustomer(String fullName, String email, String phone, String password, String address) {
        super();
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setPassword(password);
        setAddress(address);
        setRole("CUSTOMER");
        setLoyaltyPoints(100);
        setActive(true);
        this.membershipExpiry = LocalDate.now().plusYears(1);
    }

    @Override
    public double calculateDiscount(double subtotal) {
        return Math.round((subtotal * 0.10) * 100.0) / 100.0;
    }

    @Override
    public String getCustomerBadge() {
        return "VIP Gold Member";
    }

    @Override
    public int getMaxActiveBookings() {
        return 10;
    }

    @Override
    public double getDeliveryFeeDiscount() {
        return 3.00;
    }
}