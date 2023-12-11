package org.powerimo.jobs.boot2.mappers;

import org.powerimo.jobs.boot2.entities.StepEntity;
import org.powerimo.jobs.StepState;
import org.springframework.core.convert.converter.Converter;

public class StepStateConverter implements Converter<StepState, StepEntity> {

    @Override
    public StepEntity convert(StepState source) {
        return StepEntity.builder()
                .id(source.getId())
                .code(source.getStepDescriptor().getCode())
                .jobId(source.getJobId())
                .startedAt(source.getStartedAt())
                .completedAt(source.getCompletedAt())
                .status(source.getStatus())
                .result(source.getStepResult().getResult())
                .resultMessage(source.getStepResult().getMessage())
                .stepOrder(source.getStepDescriptor().getOrder())
                .countError(Long.valueOf(source.getStepResult().getCountErrors()).intValue())
                .countTotal(Long.valueOf(source.getStepResult().getCountTotal()).intValue())
                .build();
    }
}
