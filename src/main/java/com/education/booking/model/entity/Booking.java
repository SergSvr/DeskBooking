package com.education.booking.model.entity;

import com.education.booking.model.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "desk_id")
    Desk desk;

    @Column(name = "booking_date")
    LocalDateTime bookingDate;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Status status;
}
