package com.education.booking.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.education.booking.filter.CustomAuthenticationFilter;
import com.education.booking.model.dto.DeskDTO;
import com.education.booking.model.dto.UserDTO;
import com.education.booking.model.entity.User;
import com.education.booking.model.entity.Role;
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

    @GetMapping(value = "/login")
    public ModelAndView showLoginPage(ModelMap model) {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @GetMapping(value = "/index")
    public ModelAndView showIndexPage(Authentication authentication, ModelMap model) {
        model.addAttribute("name", authentication.getName());
        return new ModelAndView("index");
    }

    @GetMapping(value = "/register")
    public ModelAndView showRegisterPage(ModelMap model) {
        return new ModelAndView("register");
    }

    @PostMapping(value = "/register")
    public ModelAndView register(@ModelAttribute UserDTO userDTO) {
        log.warn(userDTO.getEmail());
        userService.createUser(userDTO);
        return new ModelAndView("register");
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
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
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

}