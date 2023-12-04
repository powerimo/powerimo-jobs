package org.powerimo.jobs.std;

import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StdInMemoryStateRepository implements StateRepository {
    private final List<JobState> jobStateList = new ArrayList<>();
    private final List<StepState> stepStateList = new ArrayList<>();

    @Override
    public Optional<JobState> getJobState(Job job) {
        return jobStateList.stream()
                .filter(item -> Objects.equals(item.getJob(), job))
                .findFirst();
    }

    @Override
    public Optional<JobState> getJobState(String id) {
        return jobStateList.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<StepState> getStepState(Step step) {
        return stepStateList.stream()
                .filter(item -> Objects.equals(item.getStep(), step))
                .findFirst();
    }

    @Override
    public void addJobState(JobState jobState) {
        jobStateList.add(jobState);
    }

    @Override
    public List<JobState> getJobStateList() {
        return jobStateList;
    }

    @Override
    public List<StepState> getStepStateList() {
        return stepStateList;
    }

    @Override
    public Optional<StepState> getStepState(String id) {
        return stepStateList.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst();
    }

    @Override
    public List<StepState> getJobStepsStateList(String jobId) {
        return stepStateList.stream()
                .filter(item -> Objects.equals(item.getJobId(), jobId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateJobState(JobState jobState) {
        if (!jobStateList.contains(jobState))
            addJobState(jobState);
    }

    @Override
    public void addStepState(StepState stepState) {
        if (!stepStateList.contains(stepState))
            stepStateList.add(stepState);
    }

    @Override
    public void updateStepState(StepState stepState) {
        if (!stepStateList.contains(stepState))
            addStepState(stepState);
    }

}
