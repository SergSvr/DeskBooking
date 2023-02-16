package com.education.booking.controllers;

import com.education.booking.model.dto.UserDTO;
import com.education.booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String hello1(@RequestParam(value = "name", defaultValue = "World", required = true) String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @GetMapping("properties")
    @ResponseBody
    java.util.Properties properties() {
        return System.getProperties();
    }

    @PostMapping
    @Operation(summary = "создать пользователя")
    public ResponseEntity<HttpStatus> createDriver(@RequestBody UserDTO userDTO) {
        userService.createDriver(userDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/user").build().toUri();
        return ResponseEntity.created(uri).build();
    }
}