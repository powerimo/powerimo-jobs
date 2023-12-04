package org.powerimo.jobs.std;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.base.AbstractRunner;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.generators.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class StdRunner extends AbstractRunner {

    public StdRunner() {
        initDefaults();
    }

    public StdRunner(RunnerConfiguration runnerConfiguration) {
        setConfiguration(runnerConfiguration);
        initDefaults();
    }

    @Override
    protected void initDefaults() {
        if (getConfiguration() == null) {
            final StdRunnerConfiguration stdRunnerConfiguration = StdRunnerConfiguration.builder()
                    .descriptorRepository(new StdDescriptorRepository())
                    .jobIdGenerator(new UuidGenerator())
                    .stepIdGenerator(new UuidGenerator())
                    .stateRepository(new StdInMemoryStateRepository())
                    .build();
            setConfiguration(stdRunnerConfiguration);
        }

        checkDescriptorRepository();
    }



/*    @Override
    public synchronized void onJobCompleted(Job job, JobResult jobResult) {
        if (getStateRepository() == null) {
            log.debug("state repository is not specified. Break onJobCompleted execution");
            return;
        }

        var stateOpt = getStateRepository().getJobState(job);
        if (stateOpt.isEmpty()) {
            log.debug("job tate is not found. Break onJobCompleted execution");
            return;
        }
        var state = stateOpt.get();

        if (!(state instanceof StdJobState)) {
            log.warn("Unsupported JobState: {}", state.getClass().getCanonicalName());
            return;
        }

        StdJobState stdState = (StdJobState) state;
        stdState.setJobResult(jobResult);
        stdState.setStatus(Status.COMPLETED);
        stdState.setCompletedAt(Instant.now());

        getStateRepository().updateJobState(stdState);

        if (isFeatureEnabled(ExecutionFeature.LOG_JOB_COMPLETED)) {
            log.info("Job completed: {}. Result={}", stdState, jobResult);
        }
    }*/

/*    @Override
    public synchronized void onStepComplete(Step step, StepResult stepResult) {
        if (getStateRepository() == null) {
            log.debug("state repository is not specified. Break onJobCompleted execution");
            return;
        }

        var stateOpt = getStateRepository().getStepState(step);
        if (stateOpt.isEmpty()) {
            log.debug("step state is not found. Break onJobCompleted execution");
            return;
        }
        var state = stateOpt.get();

        if (!(state instanceof StdStepState)) {
            log.warn("Unsupported StepState: {}", state.getClass().getCanonicalName());
            return;
        }

        final StdStepState stdState = (StdStepState) state;
        stdState.setStepResult(stepResult);
        stdState.setStatus(Status.COMPLETED);
        stdState.setCompletedAt(Instant.now());

        getStateRepository().updateStepState(stdState);

        if (isFeatureEnabled(ExecutionFeature.LOG_STEP_COMPLETED)) {
            log.info("Step completed: {}. Result={}", step, stepResult);
        }
    }*/

    @Override
    protected JobState createJobState(Job job, JobDescriptor descriptor) {
        var jobResult = StdJobResult.builder()
                .result(Result.UNKNOWN)
                .hasErrors(false)
                .build();

        var state = StdJobState.builder()
                .job(job)
                .jobDescriptor(descriptor)
                .status(Status.RUNNING)
                .startedAt(Instant.now())
                .jobResult(jobResult)
                .build();

        if (getJobIdGenerator() != null) {
            state.setId(getJobIdGenerator().getNextId());
            if (job instanceof IdSupport) {
                IdSupport idSupport = (IdSupport) job;
                idSupport.setId(state.getId());
            }
        }

        return state;
    }

    @Override
    protected JobContext createJobContext(@NonNull JobState state, Object... args) {
        var jobDescriptor = state.getJobDescriptor();

        // prepare parameters
        final List<Object> params = new ArrayList<>();
        params.add(this);
        params.add(jobDescriptor);
        params.add(state.getJob());
        params.addAll(Arrays.asList(args));

        // get steps descriptors for the job
        var stepDescriptorList = getDescriptorRepository().getStepDescriptors(jobDescriptor.getCode());

        // create context
        var context = StdJobContext.builder()
                .runner(this)
                .executionFeatures(getExecutionFeatures())
                .parameters(params)
                .jobState(state)
                .stateChangeReceiver(this)
                .stepDescriptors(stepDescriptorList)
                .build();

        log.debug("context created: {}", context);

        return context;
    }

    @Override
    protected void handleJobException(Job job, JobState jobState, Exception exception) {
        if (isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION)) {
            log.error("Exception on Job execution. JobState: {}", jobState, exception);
        }

        if (!(jobState instanceof StdJobState)) {
            log.error("JobState is not compatible with StdJobState");
            return;
        }
        final StdJobState stdJobState = (StdJobState) jobState;

        final JobResult result = StdJobResult.builder()
                .hasErrors(true)
                .result(Result.ERROR)
                .message(exception.getMessage())
                .build();

        stdJobState.setStatus(Status.COMPLETED);
        stdJobState.setCause(exception);
        stdJobState.setCompletedAt(Instant.now());
        stdJobState.setJobResult(result);

        getStateRepository().updateJobState(jobState);
    }
}
