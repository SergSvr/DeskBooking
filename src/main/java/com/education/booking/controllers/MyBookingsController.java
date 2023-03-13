package com.education.booking.controllers;

import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.entity.Booking;
import com.education.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/mybookings")
@RequiredArgsConstructor
@Tag(name = "Мои бронирования")
public class MyBookingsController {
    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "получить список бронирований пользователя")
    public ModelAndView getBooking(Authentication authentication, ModelMap model) {
        List<Booking> bookingList = bookingService.getBookingsByUser(authentication.getName());
        if(!bookingList.isEmpty())
            model.put("bookings", bookingList);
        model.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return new ModelAndView("mybookings", model);
    }

    @PostMapping(value = "/find")
    public ModelAndView findAvailableDesks(@ModelAttribute BookingDTO bookingDTO, Authentication authentication, ModelMap model) {
        List<DeskDTO> deskList = bookingService.getAvailableDesks(bookingDTO);
        model.put("desks", deskList);
        return getBooking(authentication, model);
    }
}
