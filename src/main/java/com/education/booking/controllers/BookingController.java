package com.education.booking.controllers;


import com.education.booking.model.dto.BookingDTO;
import com.education.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Бронирования")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Operation(summary = "создать бронирование")
    public BookingDTO createRoom(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime timeFrom,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime timeTo,
            String mail,
            Long deskId
    ) {
        BookingDTO bookingDTO=new BookingDTO();
        bookingDTO.setBookingDate(date);
        bookingDTO.setStartTime(timeFrom);
        bookingDTO.setEndTime(timeTo);
        return bookingService.createBooking(bookingDTO, mail, deskId);
    }

}
