package com.education.booking.model.entity;

import com.education.booking.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JsonBackReference(value="desk_bookings")
    @JoinColumn(name = "desk_id")
    Desk desk;

    @Column(name = "booking_date")
    @JsonFormat(pattern="dd.MM.yyyy")
    LocalDate bookingDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne
    @JsonBackReference(value="user_bookings")
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Status status;
}
