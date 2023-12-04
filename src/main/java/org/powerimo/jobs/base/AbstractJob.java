package org.powerimo.jobs.base;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.StepResult;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.tools.JobsUtils;
import org.powerimo.jobs.tools.StepResults;

import java.time.Instant;

@Slf4j
@Getter
@Setter
public abstract class AbstractJob extends AbstractBasicJob {
    private Class<? extends StepResult> defaultStepResultClass;
    private final StepResults stepResults = new StepResults();

    @Override
    public StepResult createResultOnException(@NonNull StepState stepState, @NonNull Exception exception) {
        if (isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION)) {
            final String prefix = JobsUtils.stepLogPrefix(getContext().getJobDescriptor(), stepState.getStepDescriptor());
            log.error("{} Exception on execute step", prefix, exception);
        }
        if (isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION_SYSTEM_OUTPUT)) {
            final String prefix = JobsUtils.stepLogPrefix(getContext().getJobDescriptor(), stepState.getStepDescriptor());
            System.out.println(prefix + " Exception on execute step");
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public void afterStepCompleted(StepState stepState) {
        stepState.setCompletedAt(Instant.now());
        stepState.setStatus(Status.COMPLETED);
        stepResults.add(stepState.getStepResult());

        if (getContext().getStateChangeReceiver() != null) {
            getContext().getStateChangeReceiver().stepCompleted(stepState, stepState.getStepResult());
        }
    }

    @Override
    protected void notifyJobResult() {
        if (getContext().getStateChangeReceiver() != null) {
            getContext().getStateChangeReceiver().jobCompleted(getContext().getJobState(), getContext().getJobState().getJobResult());
        }
    }

}
