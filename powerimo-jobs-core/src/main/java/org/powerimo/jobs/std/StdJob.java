package org.powerimo.jobs.std;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.base.AbstractJob;
import org.powerimo.jobs.exceptions.JobException;

import java.time.Instant;

@Slf4j
public class StdJob extends AbstractJob {

    @Override
    protected StepState createStepState(JobContext context, Step step, StepDescriptor descriptor) {
        String stepId = null;
        if (getContext().getRunner().getConfiguration().getStepIdGenerator() != null) {
            stepId = getContext().getRunner().getConfiguration().getStepIdGenerator().getNextId();
        }

        return StdStepState.builder()
                .id(stepId)
                .jobId(getId())
                .jobState(context.getJobState())
                .step(step)
                .stepDescriptor(descriptor)
                .startedAt(Instant.now())
                .status(Status.RUNNING)
                .result(new StdStepResult(Result.UNKNOWN, null))
                .build();
    }

    protected JobResult prepareJobResult() {
        return StdJobResult.builder()
                .result(getStepResults().hasErrors() ? Result.ERROR : Result.SUCCESS)
                .message(getStepResults().hasErrors() ? getStepResults().collectErrorMessage() : null)
                .hasErrors(getStepResults().hasErrors())
                .build();
    }

    @Override
    public StepResult createResultOnException(@NonNull StepState stepState, @NonNull Exception exception) {
        super.createResultOnException(stepState, exception);
        return StdStepResult.exception(exception);
    }

    @Override
    protected void afterJobCompletion() {
        var js = getContext().getJobState();

        if (!(js instanceof StdJobState)) {
            throw new JobException("Unsupported JobState class");
        }

        StdJobState std = (StdJobState) js;

        std.setCompletedAt(Instant.now());
        std.setStatus(Status.COMPLETED);
    }

    @Override
    protected void updateContextByStepState(StepState stepState) {
        getStdContext().getParameters()
                .removeIf(item -> item.getClass().isAssignableFrom(StepState.class));
        getStdContext().getParameters().add(stepState);
    }

    @Override
    protected void updateCurrentStep(StepState stepState) {
        final JobState jobState = getStdContext().getJobState();
        if (jobState instanceof StdJobState) {
            final StdJobState stdJobState = (StdJobState) jobState;
            stdJobState.setCurrentStep(stepState);
        }
    }

    protected StdJobContext getStdContext() {
        if (getContext() instanceof StdJobContext) {
            return (StdJobContext) getContext();
        }
        throw new JobException("Context class is not supported");
    }

}
