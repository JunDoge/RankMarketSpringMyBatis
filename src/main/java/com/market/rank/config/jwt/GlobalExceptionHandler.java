package com.market.rank.config.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomJwtExpiredException.class)
    public ResponseEntity<Object> handleCustomJwtExpiredException(CustomJwtExpiredException ex, HttpServletResponse response) throws IOException {

        System.out.println("만료된 토큰 " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token error " + "토큰만료");
    }
}
