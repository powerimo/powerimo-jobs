package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.JobContext;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepResult;

public class TestStep1 implements Step {

    @Override
    public StepResult run(JobContext jobContext) {
        return new StepResult(Result.SUCCESS, "OK");
    }

}
