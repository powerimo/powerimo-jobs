package org.powerimo.jobs;

public interface StateChangeReceiver {
    void stepCreated(JobState jobState, StepState stepState);
    void stepCompleted(StepState stepState, StepResult stepResult);
    void jobCompleted(JobState jobState, JobResult jobResult);
}
