package com.education.booking.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager manager;
    public static final int cookieTime = 1200;//seconds/refresh token+access token cookies lives 10 times longer and are replaced after cookieTime value

    public CustomAuthenticationFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("username is: {}", username);
        log.info("password is: {}", password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            return manager.authenticate(token);
        } catch (Exception e) {
            response.sendRedirect(response.encodeRedirectURL("/login?err=f"));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + cookieTime * 10000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + cookieTime * 10000L))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        setCookies(request, response, accessToken, refreshToken);
        log.info("access_token" + accessToken);
    }

    public static void setCookies(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        Cookie c1 = new Cookie("access_token", accessToken);
        c1.setSecure(false);
        c1.setDomain("127.0.0.1");
        c1.setPath("/");
        c1.setMaxAge(cookieTime * 10);
        Cookie c2 = new Cookie("refresh_token", refreshToken);
        c2.setSecure(false);
        c2.setDomain("127.0.0.1");
        c2.setPath("/");
        c2.setMaxAge(cookieTime * 10);
        response.addCookie(c1);
        response.addCookie(c2);
        String referer = request.getHeader("Referer");
        if (referer == null || referer.contains("index") || referer.contains("token") || referer.contains("login"))
            response.sendRedirect(response.encodeRedirectURL("/index"));
        else
            response.sendRedirect(referer);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        SecurityContextHolder.clearContext();
        log.warn("Failed to process authentication request" + failed);
    }

}
