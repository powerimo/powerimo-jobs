package org.powerimo.jobs;

import org.powerimo.jobs.exceptions.RunnerException;

public interface Runner {
    JobState run(String jobCode) throws RunnerException;
    JobState runArgs(String jobCode, Object... arguments) throws RunnerException;
    RunnerConfiguration getConfiguration();
}
