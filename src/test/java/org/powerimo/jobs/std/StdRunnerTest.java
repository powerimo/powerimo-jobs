package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.generators.LocalLongIdGenerator;
import org.powerimo.jobs.std.examples.TestStep1;

import static org.junit.jupiter.api.Assertions.*;

class StdRunnerTest {
    private StdRunner runner;
    private StdRepository repository;
    private static final String JOB_A01 = "A01";

    @BeforeEach
    public void init() {
        repository = new StdRepository();
        runner = new StdRunner(repository);
        runner.setStateRepository(new StdInMemoryStateRepository());

        // add Job
        repository.addJob(JOB_A01, StdJob.class);

        // add step 1 for the job
        repository.addStepDescriptor(StepDescriptor.of("STEP02", "Sample Step 2 (the same)", 2, TestStep1.class, JOB_A01));
        repository.addStepDescriptor(StepDescriptor.builder()
                .jobCode(JOB_A01)
                .code("STEP01")
                .name("Sample Step 1")
                .stepClass(TestStep1.class)
                .order(1)
                .build());
    }

    @Test
    void run_success() {
        runner.run(JOB_A01);
    }

    @Test
    void run_nonExists() {
        assertThrows(RunnerException.class, () -> runner.run("NonExisting"));
    }

    @Test
    void runner_withoutRepository() {
        final StdRunner localRunner = new StdRunner(repository);
        localRunner.setRepository(null);
        assertThrows(RunnerException.class, () -> localRunner.run(JOB_A01), "Repository is not specified");
    }

    @Test
    void runner_generatorId() {
        runner.setJobIdGenerator(new LocalLongIdGenerator());
        runner.run(JOB_A01);
    }

}