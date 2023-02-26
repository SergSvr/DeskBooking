package com.education.booking.model.entity;

import com.education.booking.model.enums.Status;
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
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "room", orphanRemoval = true, cascade = CascadeType.MERGE)
    @JsonManagedReference(value="room_desks")
    List<Desk> desks;

    Long number;
    int floor;
    String name;
    @Enumerated(EnumType.STRING)
    Status status;
}
