package com.crumb.booking.service;

import com.crumb.booking.dto.BookingRequest;
import com.crumb.booking.dto.BookingResponse;
import com.crumb.booking.dto.BookingStatusUpdate;
import com.crumb.booking.dto.SlotAvailabilityResponse;
import com.crumb.booking.entity.Booking;

import java.time.LocalDate;
import java.util.List;


// ABSTRACTION
public interface BookingService {


    BookingResponse createBooking(Long customerId, BookingRequest request);

    SlotAvailabilityResponse checkAvailability(LocalDate date);

    List<BookingResponse> getCustomerBookings(Long customerId);

    List<BookingResponse> getAllBookings(String status, String bookingType, LocalDate date);

    BookingResponse getBookingById(Long id);

    BookingResponse getBookingByReference(String ref);

    Booking getBookingEntity(Long id);

    BookingResponse updateBookingStatus(Long id, BookingStatusUpdate update);

    BookingResponse cancelBooking(Long id, Long customerId, boolean isAdmin);

    void deleteBooking(Long id);
}
