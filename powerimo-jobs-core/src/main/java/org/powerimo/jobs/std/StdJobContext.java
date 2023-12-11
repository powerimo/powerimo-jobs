package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;
import org.powerimo.jobs.features.ExecutionFeature;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class StdJobContext implements JobContext {
    private Runner runner;
    private List<StepDescriptor> stepDescriptors;
    private List<ExecutionFeature> executionFeatures;
    private JobState jobState;
    private StateChangeReceiver stateChangeReceiver;

    @Override
    public Runner getRunner() {
        return runner;
    }

    @Override
    public <T> T get(Class<T> parameterClass) {
        var obj = getJobState().getArguments().stream()
                .filter(item -> item.getClass().isAssignableFrom(parameterClass))
                .findFirst()
                .orElse(null);
        return (T) obj;
    }

    public List<Object> getParameters() {
        return getJobState().getArguments();
    }

    @Override
    public List<StepDescriptor> getStepDescriptors() {
        return stepDescriptors;
    }

    @Override
    public JobDescriptor getJobDescriptor() {
        if (jobState != null && jobState.getJobDescriptor() != null)
            return jobState.getJobDescriptor();
        return null;
    }

    @Override
    public List<ExecutionFeature> getFeatures() {
        return executionFeatures;
    }

    @Override
    public StateChangeReceiver getStateChangeReceiver() {
        return stateChangeReceiver;
    }

    @Override
    public Job getJob() {
        if (jobState == null || jobState.getJob() == null)
            throw new JobException("Job State is missing");
        return jobState.getJob();
    }

    @Override
    public JobState getJobState() {
        return jobState;
    }
}
