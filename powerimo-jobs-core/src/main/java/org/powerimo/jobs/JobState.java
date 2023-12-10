package org.powerimo.jobs;

import java.time.Instant;
import java.util.List;

public interface JobState extends IdSupport {
    Instant getStartedAt();
    Instant getCompletedAt();
    Status getStatus();
    JobResult getJobResult();
    void setJobResult(JobResult value);
    Job getJob();
    JobDescriptor getJobDescriptor();
    StepState getCurrentStep();
    List<Object> getArguments();
    <T> T getArgument(Class<T> parameterClass);
    String getTitle();
}
