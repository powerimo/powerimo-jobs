package org.powerimo.jobs;

import java.util.List;
import java.util.Optional;

public interface DescriptorRepository {
    List<JobDescriptor> getJobDescriptors();
    Optional<JobDescriptor> findJobDescriptor(String code);
    List<StepDescriptor> getStepDescriptors(String jobCode);
    void addJobDescriptor(JobDescriptor descriptor);
    void addStepDescriptor(StepDescriptor descriptor);
}
