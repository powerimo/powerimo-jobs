package org.powerimo.jobs.std;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.JobState;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.base.AbstractBasicStateRepository;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StdInMemoryStateRepository extends AbstractBasicStateRepository {
    private boolean cleanupCompleted = true;

    @Override
    public void updateJobState(JobState jobState) {
        if (jobState.getStatus() == Status.COMPLETED && cleanupCompleted) {
            var stepsToRemove = getRunningStepStateList().stream()
                    .filter(item -> Objects.equals(item.getJobState(), jobState))
                    .collect(Collectors.toList());
            getRunningStepStateList().removeAll(stepsToRemove);
            getRunningJobStateList().remove(jobState);
        }
    }

    @Override
    public void updateStepState(StepState stepState) {
        // InMemory repository doesn't require actions
    }
}
