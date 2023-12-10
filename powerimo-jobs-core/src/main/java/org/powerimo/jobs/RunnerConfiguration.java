package org.powerimo.jobs;

public interface RunnerConfiguration {
    DescriptorRepository getDescriptorRepository();
    StateRepository getStateRepository();
    IdGenerator getJobIdGenerator();
    IdGenerator getStepIdGenerator();

}
