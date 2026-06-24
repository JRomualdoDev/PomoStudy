package com.pomostudy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.dto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.awt.*;
import java.io.IOException;

@Component
public class SecurityEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public SecurityEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO body = new ErrorResponseDTO(
                "Unauthorized",
                authException.getMessage(),
                HttpStatus.UNAUTHORIZED
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
