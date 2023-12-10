package org.powerimo.jobs;

import java.util.List;
import java.util.Optional;

public interface StateRepository {
    List<JobState> getRunningJobStateList();
    List<StepState> getRunningStepStateList();
    Optional<JobState> getJobState(Job job);
    Optional<JobState> getJobState(String id);
    List<StepState> getJobStepsStateList(String jobId);
    Optional<StepState> getStepState(Step step);
    Optional<StepState> getStepState(String id);
    void addJobState(JobState jobState);
    void updateJobState(JobState jobState);
    void addStepState(StepState stepState);
    void updateStepState(StepState stepState);
}
