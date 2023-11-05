package org.powerimo.jobs;

import org.junit.jupiter.api.Test;
import org.powerimo.jobs.std.StdRunner;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    @Test
    void getRepositoryTest() {
        Runner runner = new StdRunner(null, null);

        assertNull(runner.getRepository());
        assertNull(runner.getStateRepository());
    }

}
