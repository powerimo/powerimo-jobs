package org.powerimo.jobs;

public interface Job {
    void run(JobContext context) throws Exception;
}
