package org.powerimo.jobs;

public interface JobDescriptor {
    String getCode();
    String getName();
    Class<? extends Job> getJobClass();
}
