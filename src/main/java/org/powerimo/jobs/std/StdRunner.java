package org.powerimo.jobs.std;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.generators.IdGenerator;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class StdRunner implements Runner {
    @Getter
    @Setter
    private Repository repository;

    @Getter
    @Setter
    private IdGenerator jobIdGenerator;

    @Getter
    @Setter
    private IdGenerator stepIdGenerator;

    @Getter
    @Setter
    private StateRepository stateRepository;

    public StdRunner() {
        this.repository = new StdRepository();
        this.stateRepository = new StdInMemoryStateRepository();
    }

    public StdRunner(Repository repository1) {
        repository = repository1;
        stateRepository = new StdInMemoryStateRepository();
    }

    public StdRunner(Repository repository1, StateRepository stateRepository1) {
        this.repository = repository1;
        this.stateRepository = stateRepository1;
    }

    public JobStateInfo run(@NonNull String jobCode) {
        checkRepository();

        var jobDescriptor = repository.findJobDescriptor(jobCode).orElseThrow(() -> new RunnerException("Job descriptor is not found for the code: " + jobCode));
        try {
            var jobInstance = jobDescriptor.getJobClass().getDeclaredConstructor().newInstance();

            var id = updateJobId(jobInstance);

            // prepare parameters
            List<Object> params = new ArrayList<>();
            params.add(this);
            params.add(jobDescriptor);
            params.add(jobInstance);

            // get steps descriptors for the job
            var steps = repository.getStepDescriptors(jobCode);

            // create context
            final StdJobContext context = new StdJobContext(this, params, steps);

            // run the job
            log.debug("Job is ready to execute: {}", jobInstance);
            JobStateInfo info = JobStateInfo.builder()
                    .id(id)
                    .startedAt(Instant.now())
                    .status(Status.RUNNING)
                    .build();

            if (stateRepository != null) {
                stateRepository.add(info);
            }

            CompletableFuture.runAsync(() -> {
                try {
                    jobInstance.run(context);
                } catch (Exception e) {
                    throw new RunnerException(e);
                }
            });

            log.info("Job started: {}", jobCode);
            return info;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            throw new RunnerException(e);
        }
    }

    @Override
    public synchronized void onJobCompleted(Job job, JobResult jobResult) {
        if (stateRepository != null) {
            stateRepository.onJobCompleted(job, jobResult);
            log.info("Job completed: {}. Result={}", job, jobResult);
        }
    }

    @Override
    public synchronized void onStepComplete(Step step, StepResult stepResult) {
        if (stateRepository != null) {
            stateRepository.onStepComplete(step, stepResult);
            log.info("Step completed: {}. Result={}", step, stepResult);
        }
    }

    @Override
    public synchronized void onStepCreated(Step step) {
        if (stateRepository != null)
            stateRepository.onStepCreated(step);
    }

    protected void checkRepository() {
        if (repository == null)
            throw new RunnerException("Repository is not specified");
    }

    protected String updateJobId(Job job) {
        if (jobIdGenerator != null) {
            if (job instanceof IdSupport idSupport) {
                var id = jobIdGenerator.getNextId();
                idSupport.setId(id);
                return id;
            }
        }
        return null;
    }

}
