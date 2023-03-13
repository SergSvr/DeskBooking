package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.entity.Booking;
import com.education.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
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


    @GetMapping
    @Operation(summary = "получить список бронирований")
    public ModelAndView getBooking( ModelMap model) {
        List<Booking> bookingList = bookingService.getBookings();
        model.put("bookings", bookingList);
        model.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return new ModelAndView("bookings", model);
    }

    @GetMapping("/delete")
    @Operation(summary = "удалить бронирование")
    public ModelAndView deleteBooking(@RequestParam Long id, ModelMap model) {
            bookingService.deleteBooking(id);
        return getBooking(model);
    }
}
