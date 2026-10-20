package com.crumb.crumb.service.customCake;

import com.crumb.crumb.model.customCake.CustomCakeModel;
import com.crumb.crumb.repository.customCake.CustomCakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomCakeService {

    private final CustomCakeRepository customCakeRepository;

    public CustomCakeService(CustomCakeRepository customCakeRepository) {
        this.customCakeRepository = customCakeRepository;
    }

    public CustomCakeModel createBooking(CustomCakeModel booking) {
        booking.setId(null);
        booking.setStatus("PENDING");
        return customCakeRepository.save(booking);
    }

    public List<CustomCakeModel> getAllBookings() {
        return customCakeRepository.findAll();
    }

    public CustomCakeModel getBookingById(Long id) {
        Optional<CustomCakeModel> result = customCakeRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        }

        throw new IllegalArgumentException("Custom cake booking not found: " + id);
    }

    public CustomCakeModel updateBooking(Long id, CustomCakeModel updatedBooking) {
        CustomCakeModel booking = getBookingById(id);

        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Only pending bookings can be updated");
        }

        booking.setName(updatedBooking.getName());
        booking.setPhone(updatedBooking.getPhone());
        booking.setEmail(updatedBooking.getEmail());
        booking.setOccasion(updatedBooking.getOccasion());
        booking.setSize(updatedBooking.getSize());
        booking.setDate(updatedBooking.getDate());
        booking.setFlavor(updatedBooking.getFlavor());
        booking.setFrosting(updatedBooking.getFrosting());
        booking.setDietary(updatedBooking.getDietary());
        booking.setMessage(updatedBooking.getMessage());
        booking.setNotes(updatedBooking.getNotes());
        booking.setFulfilment(updatedBooking.getFulfilment());

        return customCakeRepository.save(booking);
    }

    public CustomCakeModel cancelBooking(Long id) {
        CustomCakeModel booking = getBookingById(id);

        if (!"PENDING".equals(booking.getStatus())) {
            throw new IllegalStateException("Only pending bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");
        return customCakeRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        CustomCakeModel booking = getBookingById(id);
        customCakeRepository.delete(booking);
    }
}