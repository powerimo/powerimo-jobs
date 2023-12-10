package org.powerimo.jobs;

public interface Step {
    StepResult run(JobContext jobContext, StepDescriptor descriptor) throws Exception;
}
