package org.crazymages.bankingspringproject.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.exception.TransactionNotAllowedException;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${log.data.not.found}")
    private String dataNotFoundLogMessage;
    @Value("${log.insufficient.funds}")
    private String insufficientFundsLogMessage;
    @Value("${log.transaction.not.allowed}")
    private String transactionNotAllowedLogMessage;
    @Value("${log.illegal.argument}")
    private String illegalArgumentLogMessage;

    /**
     * Handles the {@link DataNotFoundException} exception.
     *
     * @param e the exception
     * @return the ResponseEntity with HTTP status 404
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(Exception e) {
        log.error(dataNotFoundLogMessage, e.getMessage());
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles the {@link InsufficientFundsException} exception.
     *
     * @param e the exception
     * @return the ResponseEntity with HTTP status 400
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsExceptionException(Exception e) {
        log.error(insufficientFundsLogMessage, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Handles the {@link TransactionNotAllowedException} exception.
     *
     * @return the ResponseEntity with HTTP status 400
     */
    @ExceptionHandler(TransactionNotAllowedException.class)
    public ResponseEntity<String> handleTransactionNotAllowedException() {
        log.error(transactionNotAllowedLogMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Handles the {@link IllegalArgumentException} exception.
     *
     * @return the ResponseEntity with HTTP status 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        log.error(illegalArgumentLogMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
