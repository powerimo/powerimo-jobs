package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.StepDescriptor;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.generators.LocalLongIdGenerator;
import org.powerimo.jobs.std.examples.JobArgs;
import org.powerimo.jobs.std.examples.PrintArgsStep;
import org.powerimo.jobs.std.examples.Test1Step;

import static org.junit.jupiter.api.Assertions.*;

class StdRunnerTest {
    private StdRunner runner;
    private StdRepository repository;
    private static final String JOB_A01 = "A01";

    @BeforeEach
    public void init() {
        repository = new StdRepository();
        runner = new StdRunner(repository);

        // add Job
        repository.addJob(JOB_A01, StdJob.class);

        // add step 1 for the job
        repository.addStepDescriptor(StepDescriptor.of("STEP02", "Sample Step 2 (the same)", 2, Test1Step.class, JOB_A01));
        repository.addStepDescriptor(StepDescriptor.builder()
                .jobCode(JOB_A01)
                .code("STEP01")
                .name("Sample Step 1")
                .stepClass(Test1Step.class)
                .order(1)
                .build());
    }

    @Test
    void run_success() throws NoSuchMethodException {
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
    void runner_generatorId() throws NoSuchMethodException {
        runner.setJobIdGenerator(new LocalLongIdGenerator());
        runner.run(JOB_A01);
    }

    @Test
    void runner_JobInfoTest() throws NoSuchMethodException {
        var info = runner.run(JOB_A01);

        assertNotNull(info);
        assertNotNull(info.getStartedAt());
        assertNotNull(info.getStatus());
    }

    @Test
    void runArgs_success() throws NoSuchMethodException {
        repository.addStepDescriptor(StepDescriptor.of("PRINT_ARGS", "Print args", 10, PrintArgsStep.class, JOB_A01));

        JobArgs args = new JobArgs();
        args.setParam1("aaa");
        args.setParam2(101);

        runner.runArgs(JOB_A01, args);
    }

    @Test
    void runArgs_exception() throws NoSuchMethodException, InterruptedException {
        repository.addStepDescriptor(StepDescriptor.of("PRINT_ARGS", "Print args", 1, PrintArgsStep.class, JOB_A01));
        runner.run(JOB_A01);

        Thread.sleep(300);
        assertNotNull(runner.getStateRepository());
        assertEquals(1, runner.getStateRepository().getJobStateList().size());
        var info = runner.getStateRepository().getJobStateList().get(0);

        assertEquals(Status.COMPLETED, info.getStatus());
        assertNotNull(info.getResult());
        assertEquals(Result.ERROR, info.getResult().getResult());
    }

}