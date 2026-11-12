package com.crumb.booking.repository;

import com.crumb.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    Optional<Booking> findByBookingReference(String bookingReference);

    long countByCustomerIdAndStatusNotIn(Long customerId, Collection<String> terminalStatuses);

    long countByBookingDateAndTimeSlotAndStatusNot(LocalDate bookingDate, String timeSlot, String excludedStatus);

    List<Booking> findByBookingDateOrderByTimeSlotAsc(LocalDate bookingDate);

    List<Booking> findByStatusOrderByCreatedAtDesc(String status);

    List<Booking> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
