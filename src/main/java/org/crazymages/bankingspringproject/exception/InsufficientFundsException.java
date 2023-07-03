package org.crazymages.bankingspringproject.exception;

/**
 * Exception thrown when there are insufficient funds for a specific operation.
 */
public class InsufficientFundsException extends RuntimeException {

    /**
     * Constructs a new {@code InsufficientFundsException} with the specified detail message.
     *
     * @param message the detail message
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
