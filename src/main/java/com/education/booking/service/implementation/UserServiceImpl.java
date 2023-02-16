package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.enums.Status;
import com.education.booking.model.enums.UserType;
import com.education.booking.model.repository.UserRepository;
import com.education.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectMapper mapper;


    public void createUser(UserDTO userDTO) {
        if (userDTO.getPassword().length() < 8) {
            log.error("[Create User] Password is not valid" + userDTO);
            throw new CustomException("Пароль должен быть больше 7 символов", HttpStatus.BAD_REQUEST);
        }
        try {
            InternetAddress emailAddr = new InternetAddress(userDTO.getEmail());
            emailAddr.validate();
        } catch (Exception ex) {
            log.error("[Create User] email is not valid" + userDTO);
            throw new CustomException("Невалидный email", HttpStatus.BAD_REQUEST);
        }
        userRepository.findByEmailAndStatus(userDTO.getEmail(), Status.A).ifPresent(
                driver -> {
                    log.error("[Create User] User already existed" + userDTO);
                    throw new CustomException("Пользователь с таким email уже существует", HttpStatus.BAD_REQUEST);
                }
        );
        User user = mapper.convertValue(userDTO, User.class);
        user.setStatus(Status.A);
        user.setType(UserType.User);
        User save = userRepository.save(user);
        log.info("[Created]" + save);
    }

    public void changePassword(UserDTO userDTO) {
        if (userDTO.getPassword().length() < 8)
            throw new CustomException("Пароль должен быть больше 7 символов", HttpStatus.BAD_REQUEST);
        User user = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(
                () -> {
                    log.error("[Change password] User not found" + userDTO);
                    throw new CustomException("Пользователь с таким email уже существует", HttpStatus.BAD_REQUEST);
                });
        if (user.getPassword().equals(userDTO.getPassword())) {
            log.error("[Change password] Invalid current password" + userDTO);
            throw new CustomException("Текущий пароль неверный", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(userDTO.getPassword());
        User save = userRepository.save(user);
        log.info("[Saved] " + save);
    }

    public void changeRole(Long id, UserType userType) {
        User user = getUser(id);
        user.setType(userType);
        User save=userRepository.save(user);
        log.info("[Saved] Role change " + save);
    }

    public void deleteUser(Long id){
        User user = getUser(id);
        user.setStatus(Status.C);
        userRepository.save(user);
    }

    public User getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("[Find User] User not found id=" + id);
                    throw new CustomException("Пользователь с таким id не существует", HttpStatus.BAD_REQUEST);
                }
        );
        return user;
    }

    public UserDTO getUserDTO(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("[Find User] User not found id=" + id);
                    throw new CustomException("Пользователь с таким id не существует", HttpStatus.BAD_REQUEST);
                }
        );
        return mapper.convertValue(user, UserDTO.class);
    }
}
