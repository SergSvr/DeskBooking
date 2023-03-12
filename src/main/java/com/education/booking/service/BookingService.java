package com.education.booking.service;

import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Booking;
import com.education.booking.model.entity.Desk;

import javax.transaction.Transactional;
import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO, String mail, Long id);

    void deleteBooking(Long id);

    Booking getBooking(Long id);

    List<Booking> getBookings();

    @Transactional
    List<Booking> getBookingsByUser(String mail);

    @Transactional
    List<DeskDTO> getAvailableDesks(BookingDTO bookingDTO);
}
