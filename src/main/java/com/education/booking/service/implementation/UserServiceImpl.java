package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.entity.Role;
import com.education.booking.model.enums.Status;
import com.education.booking.model.repository.RoleRepo;
import com.education.booking.model.repository.UserRepository;
import com.education.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepo roleRepo;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserDTO userDTO) {
        if (userDTO.getPassword()==null || userDTO.getPassword().length() < 8) {
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepo.findByName("ROLE_USER");
        user.getRoles().add(role);
        User save = userRepository.save(user);
        log.info("[Created]" + save);
    }

    @Override
    public User getUser(String email) {
        return userRepository.
                findByEmailAndStatus(email, Status.A).
                orElseThrow(() -> new CustomException("Пользователь с таким email не найден", HttpStatus.NOT_FOUND));
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        log.info("[Saved] " + save);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving a new role {} to db", role.getName());
        return roleRepo.save(role);
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        user.setStatus(Status.C);
        userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("[Find User] User not found id=" + id);
                    throw new CustomException("Пользователь с таким id не существует", HttpStatus.BAD_REQUEST);
                }
        );
    }

    public UserDTO getUserDTO(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("[Find User] User not found id=" + id);
                    throw new CustomException("Пользователь с таким id не существует", HttpStatus.BAD_REQUEST);
                }
        );
        return mapper.convertValue(user, UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUser(email);
        if(user == null) {
            log.error(String.format("User with name %s not found in db", email));
            throw new UsernameNotFoundException(String.format("User with name %s not found in db", email));
        } else {
            log.info(String.format("User with name %s found in db", email));
        }
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


}
