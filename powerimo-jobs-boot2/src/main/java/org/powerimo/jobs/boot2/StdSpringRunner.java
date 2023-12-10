package org.powerimo.jobs.boot2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Job;
import org.powerimo.jobs.JobDescriptor;
import org.powerimo.jobs.std.StdRunner;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor
public class StdSpringRunner extends StdRunner {
    private final ApplicationContext applicationContext;

    @Override
    protected Job createJob(JobDescriptor descriptor) {
        return applicationContext.getBean(descriptor.getJobClass());
    }
}
