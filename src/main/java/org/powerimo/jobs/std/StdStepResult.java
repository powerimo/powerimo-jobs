package org.powerimo.jobs.std;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.StepResult;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@Data
public class StdStepResult implements StepResult {
    private transient Step step;
    private transient StepDescriptor stepDescriptor;
    private String stepId;
    private String jobId;
    private Result result;
    private String message;
    private Throwable cause;
    private final AtomicLong counterRecordsTotal = new AtomicLong();
    private final AtomicLong counterErrors = new AtomicLong();

    public StdStepResult(Result result, String message) {
        this.result = result;
        this.message = message;
    }

    public static StdStepResult skipped(String message) {
        return new StdStepResult(Result.SKIPPED, message);
    }

    public static StdStepResult success(Long total, Long errors) {
        final StdStepResult stepResult = new StdStepResult();

        if (total != null)
            stepResult.counterRecordsTotal.set(total);
        if (errors != null)
            stepResult.counterErrors.set(errors);

        return stepResult;
    }

    public static StdStepResult success(Integer total, Integer errors) {
        Long totalLong = total != null ? total.longValue() : null;
        Long errorsLong = errors != null ? errors.longValue() : null;
        return success(totalLong, errorsLong);
    }

    public static StdStepResult error(String message) {
        return new StdStepResult(Result.ERROR, message);
    }

    public static StdStepResult exception(Throwable ex) {
        StdStepResult stepResult = new StdStepResult();
        stepResult.setCause(ex);
        stepResult.setResult(Result.ERROR);
        return stepResult;
    }

    @Override
    public long getCounterTotal() {
        return counterRecordsTotal.get();
    }

    @Override
    public long getCounterErrors() {
        return counterErrors.get();
    }

}
