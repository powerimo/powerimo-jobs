package org.powerimo.jobs.std;

import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class StdInMemoryStateRepository implements StateRepository {
    private final List<JobStateInfo> jobStateList = new ArrayList<>();
    private final List<StepStateInfo> stepStateList = new ArrayList<>();

    @Override
    public Optional<JobStateInfo> get(Job job) {
        return jobStateList.stream()
                .filter(item -> Objects.equals(item.getJob(), job))
                .findFirst();
    }

    @Override
    public Optional<JobStateInfo> get(String id) {
        return jobStateList.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<StepStateInfo> getStep(Step step) {
        return stepStateList.stream()
                .filter(item -> Objects.equals(item.getStep(), step))
                .findFirst();
    }

    @Override
    public JobStateInfo add(JobStateInfo jobStateInfo) {
        jobStateList.add(jobStateInfo);
        return jobStateInfo;
    }

    @Override
    public JobStateInfo add(Job job) {
        JobStateInfo info = JobStateInfo.builder()
                .startedAt(Instant.now())
                .status(Status.RUNNING)
                .job(job)
                .build();
        jobStateList.add(info);
        return info;
    }

    @Override
    public synchronized void onJobStart(Job job) {
        var opt = get(job);
        if (opt.isPresent()) {
            opt.get().setStatus(Status.RUNNING);
            opt.get().setStartedAt(Instant.now());
        } else {
            add(job);
        }
    }

    @Override
    public synchronized void onJobCompleted(Job job, JobResult result) {
        var opt = get(job);
        if (opt.isPresent()) {
            opt.get().setStatus(Status.COMPLETED);
            opt.get().setCompletedAt(Instant.now());
            opt.get().setResult(result);
        }
    }

    @Override
    public synchronized void onStepCreated(Step step) {
        final StepStateInfo stepStateInfo = StepStateInfo.builder()
                .step(step)
                .startedAt(Instant.now())
                .status(Status.RUNNING)
                .build();
        stepStateList.add(stepStateInfo);
    }

    @Override
    public synchronized void onStepComplete(Step step, StepResult stepResult) {
        var opt = getStep(step);
        if (opt.isPresent()) {
            opt.get().setCompletedAt(Instant.now());
            opt.get().setStatus(Status.COMPLETED);
        }
    }

    @Override
    public List<JobStateInfo> getJobStateList() {
        return jobStateList;
    }

    @Override
    public List<StepStateInfo> getStepStateList() {
        return stepStateList;
    }
}
