package org.powerimo.jobs.boot2.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.boot2.entities.StepEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class StepEntityConverterTest {
    private StepEntityConverter converter;
    private StepEntity entity;

    @BeforeEach
    public void init() {
        converter = new StepEntityConverter();

        entity = StepEntity.builder()
                .id("TEST_STEP_ID")
                .completedAt(Instant.now())
                .startedAt(Instant.now().minusSeconds(10))
                .build();
    }

    @Test
    void convert_success() {
        var state = converter.convert(entity);

        assertNotNull(state);
        assertEquals(entity.getId(), state.getId());
        assertEquals(entity.getStatus(), state.getStatus());
        assertEquals(entity.getStartedAt(), state.getStartedAt());
        assertEquals(entity.getCompletedAt(), state.getCompletedAt());

    }

}