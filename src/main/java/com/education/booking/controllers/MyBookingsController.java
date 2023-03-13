package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.BookingDTO;
import com.education.booking.model.dto.DeskDTO;
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
@RequestMapping("/mybookings")
@RequiredArgsConstructor
@Tag(name = "Мои бронирования")
public class MyBookingsController {
    private final BookingService bookingService;

    @ExceptionHandler({CustomException.class
    })
    public ModelAndView handler(CustomException exception,Authentication authentication) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        return getBooking(authentication, model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions(Authentication authentication) {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return getBooking(authentication, model);
    }

    @GetMapping
    @Operation(summary = "получить список бронирований пользователя")
    public ModelAndView getBooking(Authentication authentication, ModelMap model) {
        List<Booking> bookingList = bookingService.getBookingsByUser(authentication.getName());
        if (!bookingList.isEmpty())
            model.put("bookings", bookingList);
        model.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return new ModelAndView("mybookings", model);
    }

    @PostMapping(value = "/find")
    @Operation(summary = "найти бронирование")
    public ModelAndView findAvailableBooking(@ModelAttribute BookingDTO bookingDTO, Authentication authentication, ModelMap model) {

        List<DeskDTO> deskList = bookingService.getAvailableDesks(bookingDTO);
        model.put("booking",bookingDTO);
        model.put("desks", deskList);
        return getBooking(authentication, model);
    }

    @GetMapping("/delete")
    @Operation(summary = "удалить свое бронирование")
    public ModelAndView deleteBooking(@RequestParam Long id, ModelMap model, Authentication authentication) {
        Booking booking = bookingService.getBooking(id);
        if (booking.getUser().getEmail().equals(authentication.getName()))
            bookingService.deleteBooking(id);
        else {
            model.put("error","Ваше бронирование не найдено");
        }
        return getBooking(authentication,model);
    }

    @PostMapping("/book")
    @Operation(summary = "создать бронирование")
    public ModelAndView createBooking(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate date,
            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime timeFrom,
            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime timeTo,
            @RequestParam
            Long deskNumber,
            Authentication authentication,
            ModelMap model
    ) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingDate(date);
        bookingDTO.setStartTime(timeFrom);
        bookingDTO.setEndTime(timeTo);
        bookingService.createBooking(bookingDTO, authentication.getName(), deskNumber);
        return getBooking(authentication,model);
    }

}
