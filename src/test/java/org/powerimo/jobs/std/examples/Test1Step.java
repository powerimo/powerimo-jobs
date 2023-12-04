package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.*;
import org.powerimo.jobs.std.StdStepResult;

public class Test1Step implements Step {

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) {
        return new StdStepResult(Result.SUCCESS, "OK");
    }

}
