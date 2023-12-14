package org.powerimo.jobs.boot3.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powerimo.jobs.boot3.entities.JobEntity;
import org.powerimo.jobs.boot3.mappers.JobEntityConverter;

import static org.junit.jupiter.api.Assertions.*;

public class JobEntityConverterTest {
    private JobEntityConverter converter;
    private JobEntity entity;

    @BeforeEach
    public void init() {
        converter = new JobEntityConverter();

        entity = JobEntity.builder()
                .id("TEST_ENTITY_ID")
                .build();
    }

    @Test
    public void convert_success() {
        var state = converter.convert(entity);

        assertNotNull(state);
        assertEquals(entity.getId(), state.getId());
        assertEquals(entity.getCompletedAt(), state.getCompletedAt());
        assertEquals(entity.getStatus(), state.getStatus());
    }

}
