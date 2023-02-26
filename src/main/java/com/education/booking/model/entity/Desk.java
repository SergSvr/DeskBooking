package com.education.booking.model.entity;

import com.education.booking.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "desk")
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long number;
    String label;

    @OneToMany(mappedBy = "desk", orphanRemoval = true)
    @JsonManagedReference(value="desk_bookings")
    List<Booking> booking;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonBackReference(value="room_desks")
    Room room;

    @Enumerated(EnumType.STRING)
    Status status;
}
