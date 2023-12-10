package org.powerimo.jobs;

import org.powerimo.jobs.features.ExecutionFeature;

import java.util.List;

public interface JobContext {
    Runner getRunner();
    StateChangeReceiver getStateChangeReceiver();
    Job getJob();
    JobDescriptor getJobDescriptor();
    JobState getJobState();
    List<StepDescriptor> getStepDescriptors();
    <T> T get(Class<T> parameterClass);
    List<ExecutionFeature> getFeatures();
}
