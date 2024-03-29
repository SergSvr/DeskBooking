package com.education.booking.service.implementation;

import com.education.booking.exceptions.CustomException;
import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.dto.UserRoleDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.entity.Role;
import com.education.booking.model.enums.Status;
import com.education.booking.model.repository.RoleRepo;
import com.education.booking.model.repository.UserRepository;
import com.education.booking.service.BookingService;
import com.education.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.InternetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepo roleRepo;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8) {
            log.error("[Create User] Password is not valid" + userDTO);
            throw new CustomException("Пароль должен быть больше 7 символов", "create_user");
        }
        try {
            InternetAddress emailAddr = new InternetAddress(userDTO.getEmail());
            emailAddr.validate();
        } catch (Exception ex) {
            log.error("[Create User] email is not valid" + userDTO);
            throw new CustomException("Невалидный email", "create_user");
        }
        userRepository.findByEmailAndStatus(userDTO.getEmail(), Status.A).ifPresent(
                driver -> {
                    log.error("[Create User] User already existed" + userDTO);
                    throw new CustomException("Пользователь с таким email уже существует", "create_user");
                }
        );
        User user = mapper.convertValue(userDTO, User.class);
        user.setStatus(Status.A);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        log.info("[Created]" + save);
    }

    @Override
    public User getUser(String email) {
        return userRepository.
                findByEmailAndStatus(email, Status.A).
                orElseThrow(() -> new CustomException("Пользователь с таким email не найден", "get_user"));
    }

    @Override
    public UserDTO getUserDTO(String email) {
        User user = userRepository.
                findByEmailAndStatus(email, Status.A).
                orElseThrow(() -> new CustomException("Пользователь с таким email не найден", "get_user_dto"));
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPosition(user.getPosition());
        return userDTO;
    }

    @Override
    @Transactional
    public List<UserRoleDTO> getUsersRoleDTO() {
        return userRepository.
                findAllByStatus(Status.A).stream().map(user ->
                        mapper.convertValue(user, UserRoleDTO.class)
                ).collect(Collectors.toList());
    }

    @Override
    public UserDTO changeProfile(UserDTO userDTO) {
        User user = userRepository.findByEmailAndStatus(userDTO.getEmail(), Status.A).orElseThrow(
                () -> {
                    log.error("[Change password] User not found " + userDTO);
                    throw new CustomException("Пользователь с таким email уже существует", "change_profile");
                });
        //historical record
        User newUser = new User();
        newUser.setUpdatedAt(user.getUpdatedAt());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setPosition(user.getPosition());
        newUser.setStatus(Status.I);
        //-----------
        if (userDTO.getPassword().length() < 8 && userDTO.getPassword().length() > 0) {
            log.warn("Password lenght exception" + userDTO.getEmail());
            throw new CustomException("Password must be 8 symbols or more ", "change_profile");
        } else if (userDTO.getPassword().length() != 0)
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        if (userDTO.getName().length() > 0)
            user.setName(userDTO.getName());
        user.setPosition(userDTO.getPosition());
        userRepository.save(newUser);
        User save = userRepository.save(user);
        log.info("[Saved] " + save);
        return mapper.convertValue(save, UserDTO.class);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving a new role {} to db", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRole(Long id, String roleName) {
        User user = getUser(id);
        Role role = roleRepo.findByName(roleName).orElseThrow(
                () -> new CustomException("Роль не найдена", "add_role"));
        if (user.getRoles().contains(role))
            throw new CustomException("У пользователя уже есть такая роль!", "add_role");
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void deleteRole(Long id, String roleName) {
        User user = getUser(id);
        Role role = roleRepo.findByName(roleName).orElseThrow(
                () -> new CustomException("Роль не найдена", "delete_role"));
        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }
        else
            throw new CustomException("У пользователя нет такой роли роль!", "delete_role");
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUser(id);
        user.setStatus(Status.C);
        user.setUpdatedAt(LocalDateTime.now());
        user.getBooking().forEach(booking ->  booking.setStatus(Status.C));
        userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.error("[Find User] User not found id=" + id);
                    throw new CustomException("Пользователь с таким id не существует", "get_user");
                }
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUser(email);
        if (user == null) {
            log.error(String.format("User with name %s not found in db", email));
            throw new UsernameNotFoundException(String.format("User with name %s not found in db", email));
        } else {
            log.info(String.format("User with name %s found in db", email));
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


}
