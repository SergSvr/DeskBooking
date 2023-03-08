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
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static com.education.booking.filter.CustomAuthorizationFilter.readServletCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ExceptionHandler(CustomException.class)
    public ModelAndView handler(CustomException exception) {
        ModelMap model = new ModelMap();
        model.put("error", exception.getMessage());
        return showLoginPage(null, model);
    }

    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(Authentication authentication, ModelMap model) {
        getUser(authentication, model);
        if (model.getAttribute("name") != null)
            return new ModelAndView("index");
        else
            return new ModelAndView("login");
    }

    @GetMapping(value = {"/index", "/"})
    public ModelAndView showIndexPage(Authentication authentication, ModelMap model) {
        getUser(authentication, model);
        return new ModelAndView("index");
    }

    @GetMapping(value = "/register")
    public ModelAndView showRegisterPage(Authentication authentication, ModelMap model) {
        getUser(authentication, model);
        if (model.getAttribute("name") != null)
            return new ModelAndView("index");
        else
            return new ModelAndView("register");
    }

    @PostMapping(value = "/register")
    public ModelAndView register(@ModelAttribute UserDTO userDTO) {
        if (userDTO != null)
            userService.createUser(userDTO);
        return new ModelAndView("login");
    }

    @GetMapping(value = "/profile")
    public ModelAndView showProfile(Authentication authentication, ModelMap model) {
        getUser(authentication, model);
        if (model.getAttribute("name") != null)
            return new ModelAndView("index");
        else
            return new ModelAndView("register");
    }

    @PostMapping(value = "/profile")
    public ModelAndView updateProfile(@ModelAttribute UserDTO userDTO,Authentication authentication, ModelMap model) {
        getUser(authentication, model);
        if (model.getAttribute("email")!=userDTO.getEmail())
            return new ModelAndView("index");
        userService.changeProfile(userDTO);
        return new ModelAndView("profile");
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
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 100000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user
                                .getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()))
                        .sign(algorithm);

                CustomAuthenticationFilter.setCookies(response, accessToken, refreshToken);
                log.info("Refreshed access_token" + accessToken);
            } catch (Exception e) {
                log.warn("Refresh token error" + e);
                response.sendRedirect(response.encodeRedirectURL("/login"));
            }
        } else {
            log.warn("Refresh token is missing");
            response.sendRedirect(response.encodeRedirectURL("/login"));
        }
    }

    public static ModelMap getUser(Authentication authentication, ModelMap model) {
        if (authentication != null) {
            model.addAttribute("name", authentication.getName());
            if (authentication.getAuthorities().toString().contains("ROLE_ADMIN"))
                model.addAttribute("role", "admin");
        }
        return model;
    }
}