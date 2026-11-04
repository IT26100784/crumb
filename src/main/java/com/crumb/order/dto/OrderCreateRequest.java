package com.crumb.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Checkout payload submitted when placing an order.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @NotNull(message = "Order fulfillment type (DELIVERY or PICKUP) is required")
    private String orderType;

    // Delivery fields
    private String deliveryAddress;
    private String contactPhone;
    private String specialDeliveryInstructions;
    @Builder.Default
    private Double distanceKm = 4.0;

    // Pickup fields
    private String pickupTimeSlot;
    private String branchLocation;
    private String curbsideVehicleDetails;

    private String orderNotes;
}
