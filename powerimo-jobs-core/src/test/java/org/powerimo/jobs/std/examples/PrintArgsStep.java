package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;
import org.powerimo.jobs.std.StdStepResult;

public class PrintArgsStep implements Step {

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) {
        JobArgs args = jobContext.get(JobArgs.class);
        StepResult stepResult = new StdStepResult();
        if (args != null) {
            stepResult.setMessage("data: " + args);
        } else {
            throw new JobException("No args");
        }
        stepResult.setResult(Result.SUCCESS);
        return stepResult;
    }
}
