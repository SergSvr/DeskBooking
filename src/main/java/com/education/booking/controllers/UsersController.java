package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(CustomException exception) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        return showUserList(model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConversionFailedException.class, IllegalArgumentException.class})
    public ModelAndView handlerOtherExceptions() {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return showUserList(model);
    }

    @GetMapping(value = "/users")
    public ModelAndView showUserList(ModelMap model) {
        model.put("users", userService.getUsersRoleDTO());
        return new ModelAndView("users", model);
    }

    @GetMapping(value = "/users/update")
    public ModelAndView updateUser(@RequestParam String action, @RequestParam String role, @RequestParam Long id, ModelMap model) {
        if (action.equals("give")) {
            userService.addRole(id, role);
        } else if (action.equals("revoke")) {
            userService.deleteRole(id, role);
        }
        return showUserList(model);
    }

    @GetMapping(value = "/users/delete")
    public ModelAndView updateUser(@RequestParam Long id, ModelMap model) {
        userService.deleteUser(id);
        return showUserList(model);
    }
}
