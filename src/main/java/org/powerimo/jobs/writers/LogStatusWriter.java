package org.powerimo.jobs.writers;

import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Job;
import org.powerimo.jobs.StatusWriter;
import org.powerimo.jobs.Step;

@Slf4j
public class LogStatusWriter implements StatusWriter {

    @Override
    public void stepStatusChanged(Step step) {
        log.info("Step status changed: {}");
    }

    @Override
    public void jobStatusChanged(Job job) {

    }
}
