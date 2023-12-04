package org.powerimo.jobs;

import java.util.List;
import java.util.Optional;

public interface StateRepository {
    List<JobState> getJobStateList();
    List<StepState> getStepStateList();
    Optional<JobState> getJobState(Job job);
    Optional<JobState> getJobState(String id);
    Optional<StepState> getStepState(Step step);
    Optional<StepState> getStepState(String id);
    List<StepState> getJobStepsStateList(String jobId);
    void addJobState(JobState jobState);
    void updateJobState(JobState jobState);
    void addStepState(StepState stepState);
    void updateStepState(StepState stepState);
}
