package com.crumb.customcake.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("WEDDING")
@Getter
@Setter
@NoArgsConstructor
public class WeddingCake extends CustomCake {


    @Column(name = "tier_count")
    private int tierCount = 3;

    @Column(name = "has_fresh_florals")
    private boolean freshFlorals = true;

    @Column(name = "tasting_kit_ordered")
    private boolean tastingKitOrdered = false;


    @Override
    public double calculateEstimatedPrice() {
        double price = 18000.00;

        if (tierCount >= 4) {
            price = 45000.00;
        } else if (tierCount == 3) {
            price = 28000.00;
        }

        if (freshFlorals) {
            price += 3500.00;
        }

        if (tastingKitOrdered) {
            price += 2500.00;
        }

        if (getFrosting() != null && getFrosting().toLowerCase().contains("fondant")) {
            price += 2800.00;
        }

        return Math.round(price * 100.0) / 100.0;
    }


    @Override
    public String getEventSummary() {
        return "Wedding Reception Nuptials (" + tierCount + " Grand Tiered Architecture)";
    }


    @Override
    public int getPreparationLeadDays() {
        return 7; // Requires 7 days advance notice for structural curing
    }


    @Override
    public String getSpecialtyAddons() {
        return "Food-Grade Structural Doweling, Floral Cascade Styling & On-Site Cake Cutting Guide";
    }
}