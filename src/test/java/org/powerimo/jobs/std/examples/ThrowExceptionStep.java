package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.JobContext;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.StepResult;

public class ThrowExceptionStep implements Step {

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) throws Exception {
        throw new RuntimeException("Sample text of the exception");
    }

}
