package org.powerimo.jobs;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface Repository {
    List<JobDescriptor> getJobDescriptors();
    Optional<JobDescriptor> findJobDescriptor(String code);
    List<StepDescriptor> getStepDescriptors(String jobCode);
    public void addJobDescriptor(JobDescriptor descriptor);
    public void addStepDescriptor(StepDescriptor descriptor);
}
