package com.retail.rewards.exception;

/**
 * Exception thrown when transaction amount is neagtive.
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
