package org.powerimo.jobs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.std.StdStepDescriptor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepStateInfo implements IdSupport {
    private String id;
    private transient Step step;
    private transient StdStepDescriptor stepDescriptor;
    private Instant startedAt;
    private Instant completedAt;
    private Throwable cause;
    private Status status;
    private StepResult result;
}
