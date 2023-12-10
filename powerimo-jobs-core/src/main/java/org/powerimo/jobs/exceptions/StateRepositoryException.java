package org.powerimo.jobs.exceptions;

public class StateRepositoryException extends RuntimeException {
    public StateRepositoryException(String message) {
        super(message);
    }

    public StateRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateRepositoryException(Throwable cause) {
        super(cause);
    }
}
