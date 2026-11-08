package com.crumb.booking.entity;

import com.crumb.customcake.entity.CustomCake;
import com.crumb.customer.entity.Customer;
import com.crumb.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


// ABSTRACTION
// INHERITANCE
@Entity
@Table(name = "bookings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "booking_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Booking {

    // // ENCAPSULATION:
    // Attributes are declared private with Lombok getters and setters.
    // ENCAPSULATION
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_reference", nullable = false, unique = true)
    private String bookingReference;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_cake_id")
    private CustomCake customCake;

    @NotNull(message = "Booking date is required")
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "booking_fee", nullable = false)
    private double bookingFee = 0.0;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "booking_type", insertable = false, updatable = false)
    private String bookingType;


    // POLYMORPHISM
    public abstract double calculateBookingFee();


    // POLYMORPHISM
    public abstract String getBookingInstructions();


    // POLYMORPHISM
    public abstract String getBookingTypeName();
}
