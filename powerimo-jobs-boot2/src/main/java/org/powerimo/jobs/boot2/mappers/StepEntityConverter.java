package org.powerimo.jobs.boot2.mappers;

import org.powerimo.jobs.boot2.entities.StepEntity;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.std.StdStepState;
import org.springframework.core.convert.converter.Converter;

public class StepEntityConverter implements Converter<StepEntity, StepState> {

    @Override
    public StdStepState convert(StepEntity source) {
        return StdStepState.builder()
                .jobId(source.getJobId())
                .id(source.getId())
                .status(source.getStatus())
                .startedAt(source.getStartedAt())
                .completedAt(source.getCompletedAt())
                .build();
    }

}
