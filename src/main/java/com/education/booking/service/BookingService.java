package com.education.booking.service;

import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.entity.Booking;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO, String mail, Long id);

    void deleteBooking(Long id);

    Booking getBooking(Long id);
}
