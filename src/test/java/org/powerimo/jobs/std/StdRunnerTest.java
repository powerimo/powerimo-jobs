package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.std.examples.*;

import static org.junit.jupiter.api.Assertions.*;

class StdRunnerTest {
    private StdRunner runner;
    private StdDescriptorRepository descriptorRepository;
    private StdInMemoryStateRepository stateRepository;
    private StdRunnerConfiguration runnerConfiguration;
    private static final String JOB_A01 = "A01";

    @BeforeEach
    public void init() {
        descriptorRepository = new StdDescriptorRepository();
        stateRepository = new StdInMemoryStateRepository();
        runnerConfiguration = StdRunnerConfiguration.builder()
                .stateRepository(stateRepository)
                .descriptorRepository(descriptorRepository)
                .build();

        runner = new StdRunner(runnerConfiguration);
        runner.enableFeature(ExecutionFeature.LOG_EXCEPTION_SYSTEM_OUTPUT);
        runner.disableFeature(ExecutionFeature.LOG_EXCEPTION);

        // add Job
        descriptorRepository.addJob(JOB_A01, StdJob.class);

        // add step 1 for the job
        descriptorRepository.addStepDescriptor(StdStepDescriptor.of("STEP02", "Sample Step 2 (the same)", 2, Test1Step.class, JOB_A01));
        descriptorRepository.addStepDescriptor(StdStepDescriptor.builder()
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
        StdRunnerConfiguration configuration = StdRunnerConfiguration.builder()
                .descriptorRepository(descriptorRepository)
                .createMissing(false)
                .build();

        final StdRunner localRunner = new StdRunner(configuration);
        assertThrows(RunnerException.class, () -> localRunner.run(JOB_A01), "Repository is not specified");
    }

    @Test
    void runner_JobInfoTest() throws NoSuchMethodException {
        var info = runner.run(JOB_A01);

        assertNotNull(info);
        assertNotNull(info.getStartedAt());
        assertNotNull(info.getStatus());
        assertNotNull(info.getJobDescriptor());
    }

    @Test
    void runArgs_success() throws NoSuchMethodException {
        descriptorRepository.addStepDescriptor(StdStepDescriptor.of("PRINT_ARGS", "Print args", 10, PrintArgsStep.class, JOB_A01));

        JobArgs args = new JobArgs();
        args.setParam1("aaa");
        args.setParam2(101);

        runner.runArgs(JOB_A01, args);
    }

    @Test
    void runArgs_exception() throws InterruptedException {
        descriptorRepository.addStepDescriptor(StdStepDescriptor.of("PRINT_ARGS", "Print args", 1, PrintArgsStep.class, JOB_A01));
        runner.run(JOB_A01);

        Thread.sleep(300);
        assertNotNull(runner.getStateRepository());
        assertEquals(1, runner.getStateRepository().getJobStateList().size());
        var info = stateRepository.getJobStateList().get(0);

        assertEquals(Status.COMPLETED, info.getStatus());
        assertNotNull(info.getJobResult());
        assertEquals(Result.ERROR, info.getJobResult().getResult());
    }

    @Test
    void featureSupport() {
        assertFalse(runner.isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION));
        runner.enableFeature(ExecutionFeature.LOG_EXCEPTION);
        assertTrue(runner.isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION));
        runner.disableFeature(ExecutionFeature.LOG_EXCEPTION);
        assertFalse(runner.isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION));
    }

    @Test
    void stepWithException() throws NoSuchMethodException, InterruptedException {
        StdStepDescriptor descriptor = StdStepDescriptor.of("STEP03", "STEP03", 40, ThrowExceptionStep.class, JOB_A01);
        descriptorRepository.addStepDescriptor(descriptor);

        var info = runner.run(JOB_A01);
        assertNotNull(info);
        Thread.sleep(300);
        assertTrue(info.getJobResult().getHasErrors());
    }

    @Test
    void stepResultIsNull() throws NoSuchMethodException, InterruptedException {
        StdStepDescriptor descriptor = StdStepDescriptor.of("STEP_NULL", "STEP_NULL", 10, NullResultStep.class, JOB_A01);
        descriptorRepository.addStepDescriptor(descriptor);

        var info = runner.run(JOB_A01);
        assertNotNull(info);
        Thread.sleep(100);
        assertNotNull(info.getJobResult());
        assertTrue(info.getJobResult().getHasErrors());
    }

    @Test
    void incorrectDescriptor() throws NoSuchMethodException, InterruptedException {
        StdStepDescriptor descriptor = StdStepDescriptor.of("STEP_WITHOUT_CLASS", "STEP_WITHOUT_CLASS", 10, null, JOB_A01);
        descriptorRepository.addStepDescriptor(descriptor);

        var info = runner.run(JOB_A01);
        assertNotNull(info);
        Thread.sleep(100);
        assertTrue(info.getJobResult().getHasErrors());
    }
}