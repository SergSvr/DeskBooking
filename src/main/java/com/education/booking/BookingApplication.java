package com.education.booking;

import com.education.booking.model.enums.Role;
import com.education.booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.education.booking.model.enums.ERole.*;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() { // NEEDED TO ALLOW PASSWORD ENCODER INSIDE SECURITY
        return new BCryptPasswordEncoder();
    }

  /*  @Bean
    public CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, ROLE_USER));
            userService.saveRole(new Role(null, ROLE_MODERATOR));
            userService.saveRole(new Role(null, ROLE_ADMIN));
        };
    }*/

}
