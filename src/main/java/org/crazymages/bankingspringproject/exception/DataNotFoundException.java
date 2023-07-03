package org.crazymages.bankingspringproject.exception;

/**
 * Exception thrown when requested data is not found.
 */
public class DataNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code DataNotFoundException} with the specified detail message.
     *
     * @param message the detail message
     */
    public DataNotFoundException(String message) {
        super(message);
    }
}
