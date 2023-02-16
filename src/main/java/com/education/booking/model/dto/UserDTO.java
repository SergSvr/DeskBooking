package com.education.booking.model.dto;

import com.education.booking.model.enums.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String login;
    String email;
    String password;
    String newPassword;
    UserType type;
}
