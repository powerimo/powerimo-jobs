package org.powerimo.jobs.std;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.std.examples.Test1Step;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StdInMemoryStateRepositoryTest {
    private StdInMemoryStateRepository stateRepository;

    @BeforeEach
    public void init() {
        stateRepository = new StdInMemoryStateRepository();

        var job = new StdJob();
        stateRepository.onJobCreated(job);

        var step = new Test1Step();
        stateRepository.onStepCreated(step);
    }

    @Test
    void getJobStateList() {
        assertEquals(1, stateRepository.getJobStateList().size());
    }

    @Test
    void getStepStateList() {
        assertEquals(1, stateRepository.getStepStateList().size());
    }
}