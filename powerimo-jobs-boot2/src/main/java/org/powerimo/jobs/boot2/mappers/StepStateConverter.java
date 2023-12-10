package org.powerimo.jobs.boot2.mappers;

import org.powerimo.jobs.boot2.entities.StepEntity;
import org.powerimo.jobs.StepState;
import org.springframework.core.convert.converter.Converter;

public class StepStateConverter implements Converter<StepState, StepEntity> {

    @Override
    public StepEntity convert(StepState source) {
        return StepEntity.builder()
                .id(source.getId())
                .startedAt(source.getStartedAt())

                .build();
    }
}
