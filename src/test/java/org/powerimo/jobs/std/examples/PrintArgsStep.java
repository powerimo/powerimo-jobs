package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.JobContext;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepResult;
import org.powerimo.jobs.exceptions.JobException;

public class PrintArgsStep implements Step {

    @Override
    public StepResult run(JobContext jobContext) throws Exception {
        JobArgs args = jobContext.get(JobArgs.class);
        StepResult stepResult = new StepResult();
        if (args != null) {
            stepResult.setMessage("data: " + args);
        } else {
            throw new JobException("No args");
        }
        stepResult.setResult(Result.SUCCESS);
        return stepResult;
    }
}
