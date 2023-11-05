package org.powerimo.jobs;

import java.util.List;
import java.util.Optional;

public interface StateRepository {
    List<JobStateInfo> getJobStateList();
    List<StepStateInfo> getStepStateList();
    Optional<JobStateInfo> get(Job job);
    Optional<JobStateInfo> get(String id);
    Optional<StepStateInfo> getStep(Step step);
    JobStateInfo add(JobStateInfo jobStateInfo);
    JobStateInfo add(Job job);
    void onJobStart(Job job);
    void onJobCompleted(Job job, JobResult result);
    void onStepCreated(Step step);
    void onStepComplete(Step step, StepResult stepResult);
}
