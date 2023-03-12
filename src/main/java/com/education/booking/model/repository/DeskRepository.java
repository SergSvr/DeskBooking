package com.education.booking.model.repository;

import com.education.booking.model.entity.Desk;
import com.education.booking.model.entity.Room;
import com.education.booking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
    Optional<Desk> findByIdAndStatus(Long id, Status status);

    Optional<Desk> findByNumberAndStatusAndRoom(Long id, Status status, Room room);

    Desk findByNumberAndStatus(Long number, Status status);
    List<Desk> findAllByStatusOrderByRoomDesc(Status status);
}