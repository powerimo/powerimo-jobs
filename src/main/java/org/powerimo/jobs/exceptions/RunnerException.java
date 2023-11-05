package org.powerimo.jobs.exceptions;

public class RunnerException extends RuntimeException {
    public RunnerException() {
        super();
    }

    public RunnerException(String message) {
        super(message);
    }

    public RunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunnerException(Throwable cause) {
        super(cause);
    }
}
