package com.education.booking.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.education.booking.model.entity.User;
import com.education.booking.model.enums.Role;
import com.education.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static com.education.booking.filter.CustomAuthorizationFilter.readServletCookie;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value="/login")
    public ModelAndView showLoginPage(ModelMap model){
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }
    @GetMapping(value="/index")
    public ModelAndView showIndexPage(ModelMap model){
        model.put("name", "123");
        return new ModelAndView("index");
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
                                .map(Enum::name)
                                .collect(Collectors.toList()))
                        .sign(algorithm);

                Cookie c1 = new Cookie("access_token", accessToken);
                c1.setSecure(false);
                c1.setDomain("127.0.0.1");
                c1.setPath("/");
                Cookie c2 = new Cookie("refresh_token", refreshToken);
                c2.setSecure(false);
                c2.setDomain("127.0.0.1");
                c2.setPath("/");
                response.addCookie(c1);
                response.addCookie(c2);
                response.sendRedirect( response.encodeRedirectURL("/index"));
                log.info("Refreshed access_token"+accessToken);
            } catch (Exception e) {
                log.warn("Refresh token error"+e);
                response.sendRedirect(response.encodeRedirectURL("/login"));
            }
        } else {
            log.warn("Refresh token is missing");
            response.sendRedirect( response.encodeRedirectURL("/login"));
            //throw new CustomException("Refresh token is missing", HttpStatus.FORBIDDEN);
        }
    }

}