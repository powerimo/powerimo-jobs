package org.powerimo.jobs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepStateInfo {
    private Step step;
    private Instant startedAt;
    private Instant completedAt;
    private Throwable cause;
    private Status status;
    private StepResult result;
}
