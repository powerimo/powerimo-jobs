package org.powerimo.jobs.std.examples;

import org.powerimo.jobs.JobContext;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.StepResult;

public class NullResultStep implements Step {

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) throws Exception {
        return null;
    }

}
