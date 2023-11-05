package org.powerimo.jobs.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidGeneratorTest {

    @Test
    void getNextId() {
        var g = new UuidGenerator();
        assertNotNull(g.getNextId());
    }
}