package com.education.booking.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.education.booking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/token/refresh")||request.getServletPath().equals("/logout")) {
            filterChain.doFilter(request, response);
        } else {
            String token = readServletCookie(request, "access_token");
            if (!token.equals("none")) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                    log.warn("doFilterInternal: " + username);
                }catch (TokenExpiredException e) {
                    log.error("Token expired: " + e.getMessage());
                    response.sendRedirect(response.encodeRedirectURL("/token/refresh"));
                }
                catch (Exception e) {
                    log.error("Some error: " + e.getMessage());
                    clearCookie(request, response);
                    response.sendRedirect(response.encodeRedirectURL("/login"));
                }
            } else {
                clearCookie(request, response);
                filterChain.doFilter(request, response);
            }
        }
    }

    public static String readServletCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null)
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findAny().orElse("none");
        else
            return "none";
    }

    public static void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie readCookie;
        if (request.getCookies()!=null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().contains("token")) {
                    readCookie = cookie;
                    readCookie.setMaxAge(0);
                    response.addCookie(readCookie);
                }
            }
        }
    }
}
