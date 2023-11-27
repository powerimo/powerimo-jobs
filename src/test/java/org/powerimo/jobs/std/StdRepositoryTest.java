package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StdRepositoryTest {
    private final StdRepository repository = new StdRepository();
    private final String TEST_JOB_CODE = "A001";
    private final String TEST_JOB_CODE_2 = "A002";

    @BeforeEach
    private void initRepo() {
        repository.addJobDescriptor(StdJobDescriptor.builder()
                .code(TEST_JOB_CODE)
                .build());
        repository.addJobDescriptor(StdJobDescriptor.builder()
                .code(TEST_JOB_CODE_2)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s10")
                .order(10)
                .jobCode(TEST_JOB_CODE)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s5")
                .order(5)
                .jobCode(TEST_JOB_CODE)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s30")
                .order(30)
                .jobCode(TEST_JOB_CODE)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s25")
                .order(25)
                .jobCode(TEST_JOB_CODE)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s25")
                .order(10)
                .jobCode(TEST_JOB_CODE_2)
                .build());
        repository.addStepDescriptor(StdStepDescriptor.builder()
                .code("s100")
                .order(100)
                .jobCode(TEST_JOB_CODE_2)
                .build());
    }

    @Test
    void findJobDescriptor_success() {
        assertTrue(repository.findJobDescriptor(TEST_JOB_CODE).isPresent());
    }

    @Test
    void findJobDescriptor_notExists() {
        assertTrue(repository.findJobDescriptor("NonExists").isEmpty());
    }

    @Test
    void findSteps_success() {
        var data = repository.getStepDescriptors(TEST_JOB_CODE);

        assertNotNull(data);
        assertEquals(4, data.size());
        assertEquals(5, data.get(0).getOrder());
        assertEquals(10, data.get(1).getOrder());
        assertEquals(25, data.get(2).getOrder());
        assertEquals(30, data.get(3).getOrder());
    }

}