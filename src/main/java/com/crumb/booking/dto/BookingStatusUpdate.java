package com.crumb.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusUpdate {

    // ENCAPSULATION: private fields with getters/setters
    @NotBlank(message = "Status cannot be empty")
    private String status;
}
