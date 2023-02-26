package com.education.booking.model.repository;

import com.education.booking.model.entity.Room;
import com.education.booking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByFloorAndNumberAndStatus(int floor, Long number, Status status);

    Optional<Room> findByIdAndStatus(Long id, Status status);
}