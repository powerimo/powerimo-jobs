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
public class JobStateInfo {
    private String id;
    private Instant startedAt;
    private Instant completedAt;
    private Status status;
    private transient Job job;
    private JobResult result;
    private transient Throwable cause;
}
