package com.education.booking.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.awt.print.Book;
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

    String label;

    Long x;
    Long y;

    @OneToMany(mappedBy = "desk",cascade = CascadeType.ALL, orphanRemoval = true)
    List<Booking> booking;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "room_id")
    Room room;
}
