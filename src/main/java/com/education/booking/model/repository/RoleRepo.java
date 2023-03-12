package com.education.booking.model.repository;

import com.education.booking.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    @Override
    Optional<Role> findById(Long aLong);

    Optional<Role> findByName(String roleName);
}
