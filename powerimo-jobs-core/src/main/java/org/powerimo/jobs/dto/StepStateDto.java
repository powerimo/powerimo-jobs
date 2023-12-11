package org.powerimo.jobs.dto;

import lombok.Data;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.StepState;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StepStateDto {
    private Integer stepOrder;
    private String code;
    private String name;
    private StepResultDto result;
    private Instant startedAt;
    private Instant completedAt;
    private Status status;

    public StepStateDto(StepState stepState) {
        stepOrder = stepState.getStepDescriptor().getOrder();
        code = stepState.getStepDescriptor().getCode();
        name = stepState.getStepDescriptor().getName();
        result = new StepResultDto(stepState.getStepResult());
        status = stepState.getStatus();
        startedAt = stepState.getStartedAt();
        completedAt = stepState.getCompletedAt();
    }

    public static List<StepStateDto> of(List<StepState> list) {
        return list.stream()
                .map(StepStateDto::new)
                .collect(Collectors.toList());
    }

    public Duration getDuration() {
        var endTime = completedAt != null ? completedAt : Instant.now();
        return Duration.between(startedAt, endTime);
    }
}
