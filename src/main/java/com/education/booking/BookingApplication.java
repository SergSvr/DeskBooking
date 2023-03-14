package com.education.booking;

import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.Role;
import com.education.booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() { // NEEDED TO ALLOW PASSWORD ENCODER INSIDE SECURITY
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner run(UserService userService) {
        return args -> {
            //------------INIT
//            userService.saveRole(new Role(null, "ROLE_USER"));
//            userService.saveRole(new Role(null, "ROLE_ADMIN"));
//            UserDTO userDTO=new UserDTO();
//            userDTO.setName("Admin");
//            userDTO.setEmail("admin@admin.ru");
//            userDTO.setPassword("123123123");
//            userDTO.setPosition("Admin");
//            userService.createUser(userDTO);
//            Long id=userService.getUser(userDTO.getEmail()).getId();
//            userService.addRole(id,"ROLE_ADMIN");
            //------------INIT END
          //  setLoggingLevel(ch.qos.logback.classic.Level.DEBUG);//to set logs to debug level
        };
    }

    public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }
}
