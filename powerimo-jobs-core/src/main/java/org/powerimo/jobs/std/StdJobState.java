package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdJobState implements IdSupport, JobState {
    private String id;
    private Instant startedAt;
    private Instant completedAt;
    private Status status;
    private String title;
    private transient Job job;
    private transient JobDescriptor jobDescriptor;
    private JobResult jobResult;
    private transient Throwable cause;
    private transient StepState currentStep;
    private List<Object> arguments = new ArrayList<>();

    @Override
    public <T> T getArgument(Class<T> parameterClass) {
        var arg = arguments.stream()
                .filter(item -> item != null && item.getClass().isAssignableFrom(parameterClass))
                .findFirst()
                .orElse(null);
        return (T) arg;
    }
}
