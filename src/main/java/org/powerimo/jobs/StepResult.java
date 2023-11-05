package org.powerimo.jobs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@Data
public class StepResult {
    private Result result;
    private String message;
    private Throwable cause;
    private final AtomicLong counterRecordsTotal = new AtomicLong();
    private final AtomicLong counterErrors = new AtomicLong();

    public StepResult(Result result, String message) {
        this.result = result;
        this.message = message;
    }

}
