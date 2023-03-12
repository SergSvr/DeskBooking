package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.entity.Booking;
import com.education.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Бронирования")
public class BookingController {
    private final BookingService bookingService;

    @ExceptionHandler({CustomException.class
    })
    public ModelAndView handler(CustomException exception) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        return getBooking(model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions() {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return getBooking( model);
    }

    @PostMapping
    @Operation(summary = "создать бронирование")
    public ModelAndView createBooking(
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy")
            LocalDate date,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime timeFrom,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime timeTo,
            Long deskNumber,
            Authentication authentication,
            ModelMap model
    ) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingDate(date);
        bookingDTO.setStartTime(timeFrom);
        bookingDTO.setEndTime(timeTo);
        bookingService.createBooking(bookingDTO, authentication.getName(), deskNumber);
        return getBooking(model);
    }

    @GetMapping
    @Operation(summary = "получить список бронирований")
    public ModelAndView getBooking( ModelMap model) {
        List<Booking> bookingList = bookingService.getBookings();
        model.put("bookings", bookingList);
        model.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return new ModelAndView("booking", model);
    }
}
