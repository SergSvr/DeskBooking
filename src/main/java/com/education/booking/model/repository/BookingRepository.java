package com.education.booking.model.repository;

import com.education.booking.model.entity.Booking;
import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.User;
import com.education.booking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingDateAndStatusAndDesk(Date bookingDate, Status status, Desk desk);
    List<Booking> findAllByBookingDateAndStatusAndDesk(Date bookingDate, Status status, Desk desk);

    Optional<Booking> findByIdAndStatus(Long id, Status a);
}