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
import java.util.stream.Collectors;

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

        enableFeature(ExecutionFeature.LOG_STEP_COMPLETED);
        enableFeature(ExecutionFeature.LOG_JOB_COMPLETED);
        enableFeature(ExecutionFeature.LOG_EXCEPTION);

        checkDescriptorRepository();
    }

    /**
     * This method is used to create a new JobState object.
     *
     * @param job The Job object associated with the JobState.
     * @param descriptor The JobDescriptor of the Job.
     * @param arguments The arguments provided to the Job.
     * @return The newly created JobState object.
     */
    @Override
    protected JobState createJobState(Job job, JobDescriptor descriptor, Object... arguments) {
        List<Object> argList = Arrays.stream(arguments).collect(Collectors.toList());
        StdJobDisplayParameters displayParameters = getStdJobDisplayParameters(argList);

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
                .arguments(argList)
                .title(displayParameters != null ? displayParameters.getTitle() : null)
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

    /**
     * Retrieves the StdJobDisplayParameters object from the given list of arguments.
     *
     * @param argList the list of arguments
     * @return the StdJobDisplayParameters object if found, otherwise null
     */
    private static StdJobDisplayParameters getStdJobDisplayParameters(List<Object> argList) {
        var displayParametersOpt = argList.stream()
                .filter(item -> item.getClass().isAssignableFrom(StdJobDisplayParameters.class))
                .findFirst();
        StdJobDisplayParameters displayParameters = null;
        if (displayParametersOpt.isPresent()) {
            displayParameters = (StdJobDisplayParameters) displayParametersOpt.get();
        }
        return displayParameters;
    }

    /**
     * Creates a JobContext object based on the given JobState and arguments.
     *
     * @param state the JobState object
     * @param args additional arguments
     * @return the JobContext object created
     */
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
