package com.education.booking.service;

import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.entity.Role;

public interface UserService {
    void createUser(UserDTO userDTO);

    User getUser(String email);

    UserDTO getUserDTO(String email);

    UserDTO changeProfile(UserDTO userDTO);

    Role saveRole(Role role);
}
