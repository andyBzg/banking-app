package org.crazymages.bankingspringproject.exception;

public class TransactionNotAllowedException extends RuntimeException {

    public TransactionNotAllowedException() {
    }

    public TransactionNotAllowedException(String message) {
        super(message);
    }
}
