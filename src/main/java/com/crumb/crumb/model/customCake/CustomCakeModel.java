package com.crumb.crumb.model.customCake;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "custom_cake_orders")
@Getter
@Setter
@NoArgsConstructor
public class CustomCakeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 30)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Occasion is required")
    @Column(nullable = false, length = 50)
    private String occasion;

    @NotBlank(message = "Cake size is required")
    @Column(nullable = false, length = 50)
    private String size;

    @NotNull(message = "Required date is needed")
    @FutureOrPresent(message = "Required date cannot be in the past")
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank(message = "Flavor is required")
    @Column(nullable = false, length = 50)
    private String flavor;

    @NotBlank(message = "Frosting is required")
    @Column(nullable = false, length = 50)
    private String frosting;

    @NotBlank(message = "Dietary option is required")
    @Column(nullable = false, length = 50)
    private String dietary;

    @Size(max = 60, message = "Cake message must be 60 characters or less")
    @Column(length = 60)
    private String message;

    @Size(max = 1000, message = "Design notes must be 1000 characters or less")
    @Column(length = 1000)
    private String notes;

    @NotBlank(message = "Pickup or delivery is required")
    @Column(nullable = false, length = 20)
    private String fulfilment;

    @Column(nullable = false, length = 20)
    private String status;

    @PrePersist
    private void setDefaultStatus() {
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
    }
}