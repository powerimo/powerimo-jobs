package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;
import org.powerimo.jobs.features.ExecutionFeature;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class StdJobContext implements JobContext {
    private Runner runner;
    private List<Object> parameters = new ArrayList<>();
    private List<StepDescriptor> stepDescriptors = new ArrayList<>();
    private List<ExecutionFeature> executionFeatures = new ArrayList<>();
    private JobState jobState;
    private StateChangeReceiver stateChangeReceiver;

    public StdJobContext(Runner runner, List<Object> parameters, List<StepDescriptor> stepDescriptors, JobState jobState) {
        this.runner = runner;
        this.jobState = jobState;

        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
        if (stepDescriptors != null) {
            this.stepDescriptors.addAll(stepDescriptors);
        }

        if (runner instanceof StateChangeReceiver) {
            stateChangeReceiver = (StateChangeReceiver) runner;
        }
    }

    @Override
    public Runner getRunner() {
        return runner;
    }

    @Override
    public <T> T get(Class<T> parameterClass) {
        var obj = parameters.stream()
                .filter(item -> item.getClass().isAssignableFrom(parameterClass))
                .findFirst()
                .orElse(null);
        return (T) obj;
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
