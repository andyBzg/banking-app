package org.crazymages.bankingspringproject.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(Exception e) {
        log.error("Entity with id {} not found", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsExceptionException(Exception e) {
        log.error("Insufficient funds in sender's account id {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(TransactionNotAllowedException.class)
    public ResponseEntity<String> handleTransactionNotAllowedException() {
        log.error("Not allowed to execute transaction");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    //Сообщения можно убрать в файл .properties
}
