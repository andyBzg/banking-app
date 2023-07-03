package org.crazymages.bankingspringproject.exception;

/**
 * Exception thrown when a transaction is not allowed or permitted.
 */
public class TransactionNotAllowedException extends RuntimeException {

    /**
     * Constructs a new {@code TransactionNotAllowedException} with no detail message.
     */
    public TransactionNotAllowedException() {
    }

    /**
     * Constructs a new {@code TransactionNotAllowedException} with the specified detail message.
     *
     * @param message the detail message
     */
    public TransactionNotAllowedException(String message) {
        super(message);
    }
}
