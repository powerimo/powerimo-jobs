package org.powerimo.jobs.std;

import org.powerimo.jobs.Job;
import org.powerimo.jobs.JobContext;
import org.powerimo.jobs.Runner;
import org.powerimo.jobs.StepDescriptor;

import java.util.ArrayList;
import java.util.List;

public class StdJobContext implements JobContext {
    private final Runner runner;
    private final List<Object> parameters = new ArrayList<>();
    private final List<StepDescriptor> steps = new ArrayList<>();

    public StdJobContext(Runner runner, List<Object> parameters, List<StepDescriptor> stepDescriptors) {
        this.runner = runner;
        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
        if (stepDescriptors != null) {
            this.steps.addAll(stepDescriptors);
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
    public Job getJob() {
        return get(Job.class);
    }

    @Override
    public List<StepDescriptor> getStepDescriptors() {
        return steps;
    }
}
