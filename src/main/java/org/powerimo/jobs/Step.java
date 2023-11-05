package org.powerimo.jobs;

public interface Step {
    StepResult run(JobContext jobContext) throws Exception;
}
