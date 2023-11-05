package org.powerimo.jobs.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalLongIdGeneratorTest {

    @Test
    void getNextId_test() {
        var g = new LocalLongIdGenerator();

        assertEquals("1", g.getNextId());
    }

    @Test
    void constructorWithValue_test() {
        var g = new LocalLongIdGenerator(101);

        assertEquals("102", g.getNextId());
    }
}