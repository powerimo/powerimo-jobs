package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.*;

public class Test1Step implements Step {

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) {
        return new StepResult(Result.SUCCESS, "OK");
    }

}
