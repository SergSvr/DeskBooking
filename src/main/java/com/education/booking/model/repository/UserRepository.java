package com.education.booking.model.repository;

import com.education.booking.model.entity.Booking;
import com.education.booking.model.entity.User;
import com.education.booking.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(Status status);

}
