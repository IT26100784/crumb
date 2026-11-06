package com.crumb.customcake.entity;

import com.crumb.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "custom_cakes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cake_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CustomCake {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotBlank(message = "Flavor is required")
    @Column(nullable = false)
    private String flavor;

    @NotBlank(message = "Size and tier selection is required")
    @Column(name = "size_tiers", nullable = false)
    private String sizeTiers;

    @Column(nullable = false)
    private String filling = "Vanilla Buttercream";

    @Column(nullable = false)
    private String frosting = "Swiss Buttercream";

    @Column(length = 200)
    private String inscription;

    @Column(name = "color_theme")
    private String colorTheme = "Pastel Elegance";

    @Column(name = "dietary_notes")
    private String dietaryNotes = "None";

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "reference_image_url", length = 500)
    private String referenceImageUrl;

    @NotNull(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String status = "PENDING_REVIEW"; // PENDING_REVIEW, APPROVED, BAKING, READY, COMPLETED, CANCELLED

    @Column(name = "estimated_price", nullable = false)
    private double estimatedPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "cake_type", insertable = false, updatable = false)
    private String cakeType;


    public abstract double calculateEstimatedPrice();


    public abstract String getEventSummary();


    public abstract int getPreparationLeadDays();


    public abstract String getSpecialtyAddons();
}