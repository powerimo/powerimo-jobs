package org.powerimo.jobs;

public interface StatusWriter {
    void stepStatusChanged(Step step);
    void jobStatusChanged(Job job);
}
