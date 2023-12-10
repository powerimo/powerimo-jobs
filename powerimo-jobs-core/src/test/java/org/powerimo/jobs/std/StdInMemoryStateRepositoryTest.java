package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StdInMemoryStateRepositoryTest {
    private StdInMemoryStateRepository stateRepository;
    private StdJobState jobState;

    @BeforeEach
    public void init() {
        stateRepository = new StdInMemoryStateRepository();

        jobState = StdJobState.builder()
                        .build();
        stateRepository.addJobState(jobState);

        var stepState = StdStepState.builder()
                .jobState(jobState)
                .build();
        stateRepository.addStepState(stepState);
    }

    @Test
    void getJobStateList() {
        assertEquals(1, stateRepository.getRunningJobStateList().size());
    }

    @Test
    void getStepStateList() {
        assertEquals(1, stateRepository.getRunningStepStateList().size());
    }

    @Test
    void testCleanup_default() {
        jobState.setStatus(Status.COMPLETED);
        stateRepository.updateJobState(jobState);

        assertEquals(0, stateRepository.getRunningStepStateList().size());
        assertEquals(0, stateRepository.getRunningStepStateList().size());
    }

    @Test
    void testCleanup_cleanupDisabled() {
        stateRepository.setCleanupCompleted(false);
        jobState.setStatus(Status.COMPLETED);
        stateRepository.updateJobState(jobState);

        assertEquals(1, stateRepository.getRunningStepStateList().size());
        assertEquals(1, stateRepository.getRunningStepStateList().size());
    }


}