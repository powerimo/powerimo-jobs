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
    private Result result;
    private String message;
    private transient Throwable cause;
    private final AtomicLong counterTotal = new AtomicLong();
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

        stepResult.setResult(Result.SUCCESS);
        if (total != null)
            stepResult.counterTotal.set(total);
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
    public long getCountTotal() {
        return counterTotal.get();
    }

    @Override
    public long getCountErrors() {
        return counterErrors.get();
    }

    @Override
    public void setCountTotal(long value) {
        counterTotal.set(value);
    }

    @Override
    public void setCountErrors(long value) {
        counterErrors.set(value);
    }
}
