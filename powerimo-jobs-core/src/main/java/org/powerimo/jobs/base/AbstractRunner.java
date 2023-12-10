package org.powerimo.jobs.base;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.features.ExecutionFeature;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractRunner extends AbstractFeatureBasicRunner implements StateChangeReceiver {

    @Override
    public JobState runArgs(@NonNull String jobCode, Object... arguments) throws RunnerException {
        var jobDescriptor = findJobDescriptor(jobCode);

        try {
            var jobInstance = createJob(jobDescriptor);
            var jobState = createJobState(jobInstance, jobDescriptor, arguments);
            var jobContext = createJobContext(jobState, arguments);

            // run the job
            log.debug("Job is ready to execute: {}", jobState);

            if (getStateRepository() != null) {
                getStateRepository().addJobState(jobState);
            }

            CompletableFuture.runAsync(() -> {
                try {
                    log.info("Job started: {}", jobCode);
                    jobInstance.run(jobContext, jobDescriptor);
                } catch (Exception e) {
                    handleJobException(jobInstance, jobState, e);
                }
            });

            return jobState;
        } catch (RunnerException e) {
            throw e;
        } catch (Exception e) {
            throw new RunnerException(e);
        }
    }

    @Override
    public JobState run(String jobCode) throws RunnerException {
        return runArgs(jobCode);
    }

    @Override
    public synchronized void stepCreated(JobState jobState, StepState stepState) {
        if (getStateRepository() != null) {
            getStateRepository().addStepState(stepState);
        }
    }

    @Override
    public synchronized void stepCompleted(StepState stepState, StepResult stepResult) {
        if (getStateRepository() != null) {
            stepState.setStepResult(stepResult);
            getStateRepository().updateStepState(stepState);
        }

        if (isFeatureEnabled(ExecutionFeature.LOG_STEP_COMPLETED)) {
            log.info("Step completed. StepState={}; result={}", stepState, stepResult);
        }
    }

    @Override
    public synchronized void jobCompleted(JobState jobState, JobResult jobResult) {
        if (getStateRepository() != null) {
            jobState.setJobResult(jobResult);
            getStateRepository().updateJobState(jobState);
        }

        if (isFeatureEnabled(ExecutionFeature.LOG_JOB_COMPLETED)) {
            log.info("Step completed. JobState={}; result={}", jobState, jobResult);
        }
    }
}
