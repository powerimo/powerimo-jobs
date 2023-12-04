package org.powerimo.jobs;

import java.time.Instant;

public interface JobState extends IdSupport {
    Instant getStartedAt();
    Instant getCompletedAt();
    Status getStatus();
    JobResult getJobResult();
    void setJobResult(JobResult value);
    Job getJob();
    JobDescriptor getJobDescriptor();
    StepState getCurrentStep();
}
