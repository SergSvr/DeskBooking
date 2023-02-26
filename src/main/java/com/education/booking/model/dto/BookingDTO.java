package com.education.booking.model.dto;

import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDTO {
    Desk desk;
    Date bookingDate;
    LocalTime startTime;
    LocalTime endTime;
    User user;
}
