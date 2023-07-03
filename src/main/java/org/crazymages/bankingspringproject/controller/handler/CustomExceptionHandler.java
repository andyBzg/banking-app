package org.crazymages.bankingspringproject.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling custom exceptions.
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * Handles the {@link DataNotFoundException} exception.
     *
     * @param e the exception
     * @return the ResponseEntity with HTTP status 404
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(Exception e) {
        log.error("Entity with id {} not found", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles the {@link InsufficientFundsException} exception.
     *
     * @param e the exception
     * @return the ResponseEntity with HTTP status 403
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsExceptionException(Exception e) {
        log.error("Insufficient funds in sender's account id {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Handles the {@link TransactionNotAllowedException} exception.
     *
     * @return the ResponseEntity with HTTP status 403
     */
    @ExceptionHandler(TransactionNotAllowedException.class)
    public ResponseEntity<String> handleTransactionNotAllowedException() {
        log.error("Not allowed to execute transaction");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Handles the {@link IllegalArgumentException} exception.
     *
     * @return the ResponseEntity with HTTP status 403
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        log.error("One or more fields are 'null'");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    //Сообщения можно убрать в файл .properties
}
