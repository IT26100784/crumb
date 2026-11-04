package com.crumb.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DELIVERY")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryOrder extends Order {

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "special_delivery_instructions")
    private String specialDeliveryInstructions;

    @Column(name = "distance_km")
    private double distanceKm = 4.0;

    //Calculates delivery fee based on distance minus customer VIP tier delivery discount.
    @Override
    public double calculateDeliveryFee() {
        double fee = 99; // Base city delivery fee
        if (distanceKm > 3.0) {
            fee += (distanceKm - 3.0) * 50; // 50 per additional kilometer
        }

        // Apply polymorphic customer delivery discount
        if (getCustomer() != null) {
            double discount = getCustomer().getDeliveryFeeDiscount();
            fee = Math.max(0.0, fee - discount);
        }

        return Math.round(fee * 100.0) / 100.0;
    }

    @Override
    public String getDeliverySummary() {
        return "Doorstep Courier Delivery to: " + (deliveryAddress != null ? deliveryAddress : "Specified Address");
    }

    @Override
    public String getFulfillmentTimeEstimate() {
        return "Estimated delivery in 45-60 minutes via Crumb Express Courier";
    }
}
