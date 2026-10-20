package com.crumb.crumb.controller.customCake;

import com.crumb.crumb.model.customCake.CustomCakeModel;
import com.crumb.crumb.service.customCake.CustomCakeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/custom-orders")
public class CustomCakeController {

    private final CustomCakeService customCakeService;

    public CustomCakeController(CustomCakeService customCakeService) {
        this.customCakeService = customCakeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomCakeModel createBooking(@Valid @RequestBody CustomCakeModel booking) {
        return customCakeService.createBooking(booking);
    }

    @GetMapping
    public List<CustomCakeModel> getAllBookings() {
        return customCakeService.getAllBookings();
    }

    @GetMapping("/{id}")
    public CustomCakeModel getBookingById(@PathVariable Long id) {
        return customCakeService.getBookingById(id);
    }

    @PutMapping("/{id}")
    public CustomCakeModel updateBooking(@PathVariable Long id,
                                         @Valid @RequestBody CustomCakeModel booking) {
        return customCakeService.updateBooking(id, booking);
    }

    @PutMapping("/{id}/cancel")
    public CustomCakeModel cancelBooking(@PathVariable Long id) {
        return customCakeService.cancelBooking(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        customCakeService.deleteBooking(id);
    }
}