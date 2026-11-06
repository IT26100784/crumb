package com.crumb.customcake.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@DiscriminatorValue("CELEBRATION")
@Getter
@Setter
@NoArgsConstructor
public class CelebrationCake extends CustomCake {


    @Column(name = "celebration_type")
    private String celebrationType = "Anniversary"; // Anniversary, Graduation, Baby Shower, Corporate

    @Column(name = "edible_gold_leaf")
    private boolean edibleGoldLeaf = true;


    @Override
    public double calculateEstimatedPrice() {
        double price = 8500.00; // Base 6-inch

        String size = getSizeTiers() != null ? getSizeTiers().toLowerCase() : "";
        if (size.contains("8-inch")) price = 11000.00;
        else if (size.contains("10-inch")) price = 15000.00;
        else if (size.contains("2-tier")) price = 22000.00;

        if (edibleGoldLeaf) {
            price += 3500.00; // 24-Karat edible gold leaf flakes
        }

        if (getInscription() != null && !getInscription().trim().isEmpty()) {
            price += 400.00; // Custom acrylic plaque or gold script
        }

        return Math.round(price * 100.0) / 100.0;
    }


    @Override
    public String getEventSummary() {
        return celebrationType + " Milestone Celebration";
    }


    @Override
    public int getPreparationLeadDays() {
        return 3;
    }


    @Override
    public String getSpecialtyAddons() {
        return "Edible 24k Gold Leaf Highlights, Acrylic Keepsake Topper & Luxury Ribbon Wrap";
    }
}