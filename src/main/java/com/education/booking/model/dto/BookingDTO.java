package com.education.booking.model.dto;

import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDTO {
    Desk desk;
    @JsonFormat(pattern="dd.MM.yyyy")
    LocalDate bookingDate;
    LocalTime startTime;
    LocalTime endTime;
    User user;
}
