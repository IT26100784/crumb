package com.crumb.booking.controller;

import com.crumb.booking.dto.BookingRequest;
import com.crumb.booking.dto.BookingResponse;
import com.crumb.booking.dto.BookingStatusUpdate;
import com.crumb.booking.dto.SlotAvailabilityResponse;
import com.crumb.booking.service.BookingService;
import com.crumb.common.auth.SessionAuthHelper;
import com.crumb.common.dto.ApiResponse;
import com.crumb.common.exception.ForbiddenException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<SlotAvailabilityResponse>> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        SlotAvailabilityResponse availability = bookingService.checkAvailability(date);
        return ResponseEntity.ok(ApiResponse.success(availability));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request,
            HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        BookingResponse booking = bookingService.createBooking(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed! Reference: " + booking.getBookingReference(), booking));
    }


    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(HttpSession session) {
        Long customerId = SessionAuthHelper.requireLoggedIn(session);
        List<BookingResponse> list = bookingService.getCustomerBookings(customerId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        BookingResponse booking = bookingService.getBookingById(id);
        if (!isAdmin && !booking.getCustomerId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to view this booking.");
        }

        return ResponseEntity.ok(ApiResponse.success(booking));
    }


    @GetMapping("/ref/{ref}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByRef(@PathVariable String ref) {
        BookingResponse booking = bookingService.getBookingByReference(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id,
            HttpSession session) {
        Long currentUserId = SessionAuthHelper.requireLoggedIn(session);
        boolean isAdmin = SessionAuthHelper.isAdmin(session);

        BookingResponse cancelled = bookingService.cancelBooking(id, currentUserId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully.", cancelled));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String bookingType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        List<BookingResponse> bookings = bookingService.getAllBookings(status, bookingType, date);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdate update,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        BookingResponse updated = bookingService.updateBookingStatus(id, update);
        return ResponseEntity.ok(ApiResponse.success("Booking status updated to " + updated.getStatus(), updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(
            @PathVariable Long id,
            HttpSession session) {
        SessionAuthHelper.requireAdmin(session);
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking record removed.", null));
    }
}
