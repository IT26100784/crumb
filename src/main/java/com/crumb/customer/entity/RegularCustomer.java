package com.crumb.customer.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("REGULAR")
@Getter
@Setter
@NoArgsConstructor
public class RegularCustomer extends Customer {

    private String preferredBranch = "Main Bakery";

    public RegularCustomer(String fullName, String email, String phone, String password, String address) {
        super();
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setPassword(password);
        setAddress(address);
        setRole("CUSTOMER");
        setLoyaltyPoints(0);
        setActive(true);
    }

    @Override
    public double calculateDiscount(double subtotal) {
        return 0.0;
    }

    @Override
    public String getCustomerBadge() {
        return "Classic Member";
    }

    @Override
    public int getMaxActiveBookings() {
        return 3;
    }

    @Override
    public double getDeliveryFeeDiscount() {
        return 0.0;
    }
}