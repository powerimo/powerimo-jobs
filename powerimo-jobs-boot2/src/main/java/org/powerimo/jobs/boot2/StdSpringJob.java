package org.powerimo.jobs.boot2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.std.StdJob;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
@RequiredArgsConstructor
@Slf4j
public class StdSpringJob extends StdJob {
    private final ApplicationContext applicationContext;

    @Override
    protected Step createStep(StepDescriptor descriptor) throws RunnerException {
        try {
            return applicationContext.getBean(descriptor.getStepClass());
        } catch (NoSuchBeanDefinitionException ex) {
            log.info("No bean was found: " + descriptor.getStepClass() + "; Standard class will be used.");
        }

        // try to resolve to regular class
        return super.createStep(descriptor);
    }
}
