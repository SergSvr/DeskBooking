package com.education.booking.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.education.booking.exceptions.CustomException;
import com.education.booking.filter.CustomAuthenticationFilter;
import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.Role;
import com.education.booking.model.entity.User;
import com.education.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.education.booking.filter.CustomAuthorizationFilter.clearCookie;
import static com.education.booking.filter.CustomAuthorizationFilter.readServletCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(Authentication authentication, CustomException exception) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        if (Objects.equals(exception.getAction(), "create_user"))
            return showRegisterPage(authentication, model);
        else if (Objects.equals(exception.getAction(), "change_profile")) {
            return showProfile(authentication, model);
        }
        return showLoginPage(authentication, model);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            IllegalArgumentException.class
    })
    public ModelAndView handlerOtherExceptions(Authentication authentication) {
        ModelMap model = new ModelMap();
        model.put("error", "Введены некорректные данные");
        return showLoginPage(authentication, model);
    }

    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(Authentication authentication, ModelMap model) {
        if (authentication!= null)
            return new ModelAndView("index", model);
        else
            return new ModelAndView("login", model);
    }

    @GetMapping(value = {"/index", "/"})
    public ModelAndView showIndexPage(ModelMap model) {
        return new ModelAndView("index", model);
    }

    @GetMapping(value = "/register")
    public ModelAndView showRegisterPage(Authentication authentication, ModelMap model) {
        if (authentication != null)
            return new ModelAndView("index");
        else
            return new ModelAndView("register", model);
    }

    @PostMapping(value = "/register")
    public ModelAndView register(@ModelAttribute UserDTO userDTO) {
        if (userDTO != null)
            userService.createUser(userDTO);
        return new ModelAndView("login");
    }

    @GetMapping(value = "/profile")
    public ModelAndView showProfile(Authentication authentication, ModelMap model) {
        if (authentication != null) {
            UserDTO userDTO = userService
                    .getUserDTO(authentication.getPrincipal().toString());
            model.put("user", userDTO);
            return new ModelAndView("profile", model);
        }
        log.warn("Unauthorized access!");
        return new ModelAndView("register", model);
    }

    @PostMapping(value = "/profile")
    public ModelAndView updateProfile(@ModelAttribute UserDTO userDTO, Authentication authentication, ModelMap model) {
        if (authentication == null)
            return new ModelAndView("index");
        else {
            userDTO.setEmail(authentication.getName());
            userService.changeProfile(userDTO);
            return showProfile(authentication, model);
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = readServletCookie(request, "refresh_token");
        if (!refreshToken.equals("none")) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                User user = userService.getUser(email);
                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + CustomAuthenticationFilter.cookieTime*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user
                                .getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()))
                        .sign(algorithm);
                String refreshTokenNew = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + CustomAuthenticationFilter.cookieTime*10000L))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);
                CustomAuthenticationFilter.setCookies(request, response, accessToken, refreshTokenNew);
                log.info("Refreshed access_token" + accessToken);
                log.info("Refreshed refresh_token" + refreshTokenNew);
            } catch (Exception e) {
                log.warn("Refresh token error" + e);
                clearCookie(request, response);
                response.sendRedirect(response.encodeRedirectURL("/login"));
            }
        } else {
            log.warn("Refresh token is missing");
            clearCookie(request, response);
            response.sendRedirect(response.encodeRedirectURL("/login"));
        }
    }
}