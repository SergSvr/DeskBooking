package com.education.booking.controllers;

import com.education.booking.exceptions.CustomException;
import com.education.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.security.core.Authentication;
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
    public ModelAndView handler(Authentication authentication, CustomException exception) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        return showUserList(authentication, model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions(Authentication authentication) {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return showUserList(authentication, model);
    }

    @GetMapping(value = "/users")
    public ModelAndView showUserList(Authentication authentication, ModelMap model) {
        model.put("users", userService.getUsersRoleDTO());
        return new ModelAndView("users", model);
    }

    @GetMapping(value = "/users/update")
    public ModelAndView updateUser(@RequestParam String action,
                                   @RequestParam String role,
                                   @RequestParam Long id,
                                   Authentication authentication, ModelMap model) {
        if (action.equals("give")) {
            userService.addRole(id, role);
        } else if (action.equals("revoke")) {
            userService.deleteRole(id, role);
        }
        return showUserList(authentication, model);
    }

    @GetMapping(value = "/users/delete")
    public ModelAndView updateUser(@RequestParam Long id,
                                   Authentication authentication, ModelMap model) {
        userService.deleteUser(id);
        return showUserList(authentication, model);
    }
}
