package org.powerimo.jobs;

import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.std.StdStepDescriptor;

import java.util.List;

public interface JobContext {
    Runner getRunner();
    Job getJob();
    JobDescriptor getJobDescriptor();
    List<StepDescriptor> getStepDescriptors();
    <T> T get(Class<T> parameterClass);
    List<ExecutionFeature> getFeatures();
}
