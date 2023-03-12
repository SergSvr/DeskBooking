package com.education.booking.model.dto;

import com.education.booking.model.entity.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleDTO {
    Long id;
    String email;
    String password;
    String name;
    String position;
    Collection<Role> roles;
}
