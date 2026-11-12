package com.crumb.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// INHERITANCE
@Entity
@DiscriminatorValue("PICKUP")
@Getter
@Setter
@NoArgsConstructor
public class PickupBooking extends Booking {

    // ENCAPSULATION: private fields with Lombok accessors
    @Column(name = "pickup_branch")
    private String pickupBranch = "Crumb Flagship Bakery, Colombo 03";

    @Column(name = "party_size")
    private int partySize = 1;

    @Column(name = "is_tasting_table_reserved")
    private boolean tastingTableReserved = false;


    // POLYMORPHISM
    @Override
    public double calculateBookingFee() {

        return tastingTableReserved ? 10.00 : 0.00;
    }

    // POLYMORPHISM
    @Override
    public String getBookingInstructions() {
        return "Visit " + pickupBranch + " during your selected slot [" + getTimeSlot() +
                "]. Present reference '" + getBookingReference() + "' at Counter A.";
    }

    // POLYMORPHISM
    @Override
    public String getBookingTypeName() {
        return "In-Store Counter Pickup";
    }
}
