package org.crazymages.bankingspringproject.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(Exception e) {
        log.error("Entity with uuid {} not found", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
