package org.powerimo.jobs.base;

import lombok.Getter;
import org.powerimo.jobs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public abstract class AbstractBasicStateRepository implements StateRepository {
    private final List<JobState> runningJobStateList = new ArrayList<>();
    private final List<StepState> runningStepStateList = new ArrayList<>();

    public Optional<JobState> getRunningJobState(Job job) {
        return runningJobStateList.stream()
                .filter(item -> Objects.equals(item.getJob(), job))
                .findFirst();
    }
    public Optional<JobState> getRunningJobState(String id) {
        return runningJobStateList.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<JobState> getJobState(String id) {
        return getRunningJobState(id);
    }

    @Override
    public Optional<StepState> getStepState(Step step) {
        return runningStepStateList.stream()
                .filter(item -> item.getStep() == step)
                .findFirst();
    }

    public Optional<StepState> getRunningStepState(String id) {
        return runningStepStateList.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<StepState> getStepState(String id) {
        return getRunningStepState(id);
    }

    @Override
    public void addJobState(JobState jobState) {
        runningJobStateList.add(jobState);
    }

    @Override
    public void addStepState(StepState stepState) {
        runningStepStateList.add(stepState);
    }

    @Override
    public Optional<JobState> getJobState(Job job) {
        return getRunningJobState(job);
    }

    @Override
    public List<StepState> getJobStepsStateList(String jobId) {
        // check the job is existing
        getJobState(jobId).orElseThrow();
        return runningStepStateList.stream()
                .filter(item -> Objects.equals(item.getJobId(), jobId))
                .collect(Collectors.toList());
    }
}
