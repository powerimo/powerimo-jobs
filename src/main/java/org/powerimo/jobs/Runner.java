package org.powerimo.jobs;

public interface Runner {
    JobStateInfo run(String jobCode) throws NoSuchMethodException;
    JobStateInfo runArgs(String jobCode, Object... arguments) throws NoSuchMethodException;
    void onJobCompleted(Job job, JobResult jobResult);
    void onStepCreated(Step step);
    void onStepComplete(Step step, StepResult stepResult);
    StateRepository getStateRepository();
    Repository getRepository();
}
