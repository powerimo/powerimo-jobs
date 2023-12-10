package org.powerimo.jobs.complextest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.std.*;
import org.powerimo.jobs.std.examples.Test1Step;
import org.powerimo.jobs.std.steps.StdJobBannerStep;
import org.powerimo.jobs.std.steps.StdLogJobStatStep;

public class StdJobBannerStepTest {
    private StdRunner runner;
    private StdDescriptorRepository repository;
    private static final String JOB_A01 = "A01";

    @BeforeEach
    public void init() {
        repository = new StdDescriptorRepository();

        // add Job
        repository.addJobDescriptor(
                StdJobDescriptor.builder()
                        .jobClass(StdJob.class)
                        .code(JOB_A01)
                        .name("StdJobBannerStepTest")
                        .build());

        // add step 1 for the job
        repository.addStepDescriptor(StdStepDescriptor.of("STEP02", "Sample Step 2 (the same)", 2, Test1Step.class, JOB_A01));
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .jobCode(JOB_A01)
                .code("STEP01")
                .name("Sample Step 1")
                .stepClass(StdJobBannerStep.class)
                .order(1)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .jobCode(JOB_A01)
                .code("STEP_STAT")
                .name("Steps stat step")
                .order(100)
                .stepClass(StdLogJobStatStep.class)
                .build());

        StdRunnerConfiguration configuration = StdRunnerConfiguration.builder()
                .descriptorRepository(repository)
                .build();

        runner = new StdRunner(configuration);
        runner.enableFeature(ExecutionFeature.LOG_SYSTEM_OUTPUT);
        runner.enableFeature(ExecutionFeature.LOG_JOB_COMPLETED);
    }

    @Test
    public void statStep_test() throws NoSuchMethodException {
        runner.run(JOB_A01);
    }
}
