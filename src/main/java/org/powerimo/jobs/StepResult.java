package org.powerimo.jobs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@Data
public class StepResult {
    private transient Step step;
    private transient StepDescriptor stepDescriptor;
    private String stepId;
    private String jobId;
    private Result result;
    private String message;
    private Throwable cause;
    private final AtomicLong counterRecordsTotal = new AtomicLong();
    private final AtomicLong counterErrors = new AtomicLong();

    public StepResult(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public static StepResult skipped(String message) {
        return new StepResult(Result.SKIPPED, message);
    }

    public static StepResult error(String message) {
        return new StepResult(Result.ERROR, message);
    }

    public static StepResult exception(Throwable ex) {
        StepResult stepResult = new StepResult();
        stepResult.setCause(ex);
        stepResult.setResult(Result.ERROR);
        return stepResult;
    }

}
