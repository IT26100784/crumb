package com.crumb.customcake.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomCakeRequest {


    @NotBlank(message = "Flavor choice is required")
    private String flavor;

    @NotBlank(message = "Size / tier choice is required")
    private String sizeTiers;

    private String filling;
    private String frosting;
    private String inscription;
    private String colorTheme;
    private String dietaryNotes;
    private String specialInstructions;
    private String referenceImageUrl;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate eventDate;

    @NotBlank(message = "Cake category (BIRTHDAY, WEDDING, CELEBRATION) is required")
    private String cakeType;


    private Integer ageNumber;
    @Builder.Default
    private Boolean candleSetIncluded = true;
    private String characterTheme;


    @Builder.Default
    private Integer tierCount = 3;
    @Builder.Default
    private Boolean freshFlorals = true;
    @Builder.Default
    private Boolean tastingKitOrdered = false;


    private String celebrationType;
    @Builder.Default
    private Boolean edibleGoldLeaf = true;
}