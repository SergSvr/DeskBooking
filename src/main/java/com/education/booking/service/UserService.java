package com.education.booking.service;

import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.dto.UserRoleDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.entity.Role;

import java.util.List;

public interface UserService {
    void createUser(UserDTO userDTO);

    User getUser(String email);

    UserDTO getUserDTO(String email);

    List<UserRoleDTO> getUsersRoleDTO();

    UserDTO changeProfile(UserDTO userDTO);

    Role saveRole(Role role);

    void addRole(Long id, String roleName);

    void deleteRole(Long id, String roleName);

    void deleteUser(Long id);
}
