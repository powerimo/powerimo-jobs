package org.powerimo.jobs;

import java.util.List;

public interface JobContext {
    Runner getRunner();
    Job getJob();
    List<StepDescriptor> getStepDescriptors();
    <T> T get(Class<T> parameterClass);
}
