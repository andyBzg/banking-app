package org.crazymages.bankingspringproject.controller.handler;

import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;

    @Test
    void handleDataNotFoundException_shouldReturnNotFoundResponse() {
        // given
        DataNotFoundException exception = new DataNotFoundException("Data not found");

        // when
        ResponseEntity<String> response = customExceptionHandler.handleDataNotFoundException(exception);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleInsufficientFundsException_shouldReturnBadRequestResponse() {
        // given
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds");

        // when
        ResponseEntity<String> response = customExceptionHandler.handleInsufficientFundsExceptionException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleTransactionNotAllowedException_shouldReturnBadRequestResponse() {
        // when
        ResponseEntity<String> response = customExceptionHandler.handleTransactionNotAllowedException();

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequestResponse() {
        // when
        ResponseEntity<String> response = customExceptionHandler.handleIllegalArgumentException();

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
