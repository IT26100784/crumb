package com.crumb.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// INHERITANCE
@Entity
@DiscriminatorValue("DELIVERY")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryBooking extends Booking {

    // ENCAPSULATION: private fields with Lombok accessors
    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "recipient_contact_phone")
    private String recipientContactPhone;

    @Column(name = "is_white_glove_setup")
    private boolean whiteGloveSetup = false;


    // POLYMORPHISM
    @Override
    public double calculateBookingFee() {
        double fee = 5.00;
        if (whiteGloveSetup) {
            fee += 20.00; // Cake stand assembly, chilling check, on-site staging
        }

        if (getCustomer() != null) {
            double discount = getCustomer().getDeliveryFeeDiscount();
            fee = Math.max(0.0, fee - discount);
        }

        return Math.round(fee * 100.0) / 100.0;
    }

    // POLYMORPHISM
    @Override
    public String getBookingInstructions() {
        return "Courier dispatch scheduled to " + (destinationAddress != null ? destinationAddress : "Your Address") +
                " during window [" + getTimeSlot() + "]. Contact recipient at: " +
                (recipientContactPhone != null ? recipientContactPhone : "Provided Phone");
    }

    // POLYMORPHISM
    @Override
    public String getBookingTypeName() {
        return "Scheduled Courier Delivery";
    }
}
