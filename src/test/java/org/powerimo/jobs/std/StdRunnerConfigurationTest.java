package org.powerimo.jobs.std;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StdRunnerConfigurationTest {

    @Test
    void builder_defaults() {
        var config = StdRunnerConfiguration.builder()
                .build();

        assertNotNull(config);
        assertNotNull(config.getJobIdGenerator());
        assertNotNull(config.getStepIdGenerator());
        assertNotNull(config.getStateRepository());
        assertNotNull(config.getDescriptorRepository());
    }


}