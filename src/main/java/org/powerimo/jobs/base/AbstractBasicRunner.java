package org.powerimo.jobs.base;

import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.RunnerException;

@Getter
public abstract class AbstractBasicRunner implements Runner {
    @Setter
    private RunnerConfiguration configuration;

    @Override
    public abstract JobState run(String jobCode) throws RunnerException;

    @Override
    public abstract JobState runArgs(String jobCode, Object... arguments) throws RunnerException;

    protected abstract JobState createJobState(Job job, JobDescriptor descriptor);

    protected abstract JobContext createJobContext(JobState state, Object... args);

    protected abstract void handleJobException(Job job, JobState jobState, Exception exception);

    protected abstract void initDefaults();

    public DescriptorRepository getDescriptorRepository() {
        if (configuration != null && configuration.getDescriptorRepository() != null)
            return configuration.getDescriptorRepository();
        throw new RunnerException("DescriptorRepository is not specified");
    }

    public StateRepository getStateRepository() {
        if (configuration != null && configuration.getStateRepository() != null)
            return configuration.getStateRepository();
        throw new RunnerException("StateRepository is not specified");
    }

    public IdGenerator getJobIdGenerator() {
        if (configuration != null)
            return configuration.getJobIdGenerator();
        return null;
    }

    protected IdGenerator getStepIdGenerator() {
        if (configuration != null)
            return configuration.getStepIdGenerator();
        return null;
    }

    protected Job createJob(JobDescriptor descriptor) throws Exception {
        return descriptor.getJobClass().getDeclaredConstructor().newInstance();
    }

    protected void checkConfiguration() {
        if (configuration == null)
            throw new RunnerException("Runner is not configured (configuration is not specified)");
    }

    protected void checkDescriptorRepository() {
        checkConfiguration();
        if (getConfiguration().getDescriptorRepository() == null)
            throw new RunnerException("DescriptorRepository is not specified");
    }

    protected JobDescriptor findJobDescriptor(String jobDescriptorId) {
        checkDescriptorRepository();
        return getDescriptorRepository().findJobDescriptor(jobDescriptorId).orElseThrow(() -> new RunnerException("Job descriptor is not found for the code: " + jobDescriptorId));
    }


}
