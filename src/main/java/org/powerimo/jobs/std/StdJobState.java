package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdJobState implements IdSupport, JobState {
    private String id;
    private Instant startedAt;
    private Instant completedAt;
    private Status status;
    private transient Job job;
    private transient JobDescriptor jobDescriptor;
    private JobResult jobResult;
    private transient Throwable cause;
    private transient StepState currentStep;
}
