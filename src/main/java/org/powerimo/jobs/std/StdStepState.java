package org.powerimo.jobs.std;

import lombok.*;
import org.powerimo.jobs.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdStepState implements StepState {
    private String id;

    @Setter
    private String jobId;

    @Setter
    private transient Step step;

    @Setter
    private transient StepDescriptor stepDescriptor;

    private Instant startedAt;
    private Instant completedAt;
    private Throwable cause;
    private Status status;
    private StepResult result;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getJobId() {
        return jobId;
    }

    @Override
    public StepResult getStepResult() {
        return result;
    }

    @Override
    public Instant getStartedAt() {
        return startedAt;
    }

    @Override
    public Instant getCompletedAt() {
        return completedAt;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Step getStep() {
        return step;
    }

    @Override
    public StepDescriptor getStepDescriptor() {
        return stepDescriptor;
    }

    @Override
    public void setStepResult(StepResult value) {
        result = value;
    }

    @Override
    public void setStartedA(Instant value) {
        startedAt = value;
    }

    @Override
    public void setCompletedAt(Instant value) {
        completedAt = value;
    }

    @Override
    public void setStatus(Status value) {
        status = value;
    }
}
