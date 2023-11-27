package org.powerimo.jobs.std;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Job;
import org.powerimo.jobs.JobDescriptor;
import org.powerimo.jobs.Repository;
import org.powerimo.jobs.StepDescriptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class StdRepository implements Repository {
    private final ArrayList<JobDescriptor> jobDescriptors = new ArrayList<>();
    private final ArrayList<StepDescriptor> stepDescriptors = new ArrayList<>();

    @Override
    public Optional<JobDescriptor> findJobDescriptor(@NonNull String code) {
        return jobDescriptors.stream().filter((item) -> item.getCode().equals(code)).findFirst();
    }

    @Override
    public List<StepDescriptor> getStepDescriptors(@NonNull String jobCode) {
        return stepDescriptors.stream()
                .filter(item -> item.getJobCode().equals(jobCode))
                .sorted(Comparator.comparingInt(StepDescriptor::getOrder))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDescriptor> getJobDescriptors() {
        return jobDescriptors;
    }

    @Override
    public void addJobDescriptor(@NonNull JobDescriptor descriptor) {
        jobDescriptors.add(descriptor);
    }

    public void addJob(String code, Class<? extends Job> jobClass) {
        StdJobDescriptor descriptor = new StdJobDescriptor();
        descriptor.setCode(code);
        descriptor.setJobClass(jobClass);
        addJobDescriptor(descriptor);
    }

    @Override
    public void addStepDescriptor(@NonNull StepDescriptor descriptor) {
        stepDescriptors.add(descriptor);
    }


}
