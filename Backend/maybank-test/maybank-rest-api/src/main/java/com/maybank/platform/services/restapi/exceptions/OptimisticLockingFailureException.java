package com.maybank.platform.services.restapi.exceptions;

public class OptimisticLockingFailureException extends RuntimeException {
    public OptimisticLockingFailureException(String message) {
        super(message);
    }
}
