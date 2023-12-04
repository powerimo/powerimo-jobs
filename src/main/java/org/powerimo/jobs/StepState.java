package org.powerimo.jobs;

import java.time.Instant;

public interface StepState extends IdSupport {
    String getJobId();
    StepResult getStepResult();
    void setStepResult(StepResult value);

    Instant getStartedAt();
    void setStartedA(Instant value);

    Instant getCompletedAt();
    void setCompletedAt(Instant value);

    Status getStatus();
    void setStatus(Status value);

    Step getStep();
    StepDescriptor getStepDescriptor();
}
