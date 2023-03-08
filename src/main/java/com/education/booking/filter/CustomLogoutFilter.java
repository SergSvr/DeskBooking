package com.education.booking.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.education.booking.filter.CustomAuthorizationFilter.clearCookie;

@Slf4j
@Service
public class CustomLogoutFilter implements LogoutHandler {


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        clearCookie(request, response);
        try {
            response.sendRedirect("/index");
        } catch (IOException e) {
            log.error("Logout error");
        }
        log.info("Logout process");
    }

}
