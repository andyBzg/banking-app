package org.crazymages.bankingspringproject.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataNotFoundExceptionTest {

    @Test
    void constructor_withMessage_shouldSetMessage() {
        // given
        String message = "Data not found";

        // when
        DataNotFoundException exception = new DataNotFoundException(message);

        // then
        assertEquals(message, exception.getMessage());
    }
}
