package org.powerimo.jobs.base;

import lombok.Getter;
import org.powerimo.jobs.JobState;
import org.powerimo.jobs.StepState;

import java.util.List;
import java.util.Optional;

@Getter
public abstract class AbstractPersistentStateRepository extends AbstractBasicStateRepository {

    public abstract Optional<JobState> getPersistentJobState(String id);
    public abstract Optional<StepState> getPersistentStepState(String id);
    public abstract void addPersistentJobState(JobState jobState);
    public abstract void updatePersistentJobState(JobState jobState);
    public abstract void addPersistentStepState(StepState stepState);
    public abstract void updatePersistentStepState(StepState stepState);

    @Override
    public Optional<JobState> getJobState(String id) {
        var result = getRunningJobState(id);
        if (result.isPresent())
            return result;
        return getPersistentJobState(id);
    }


    @Override
    public Optional<StepState> getStepState(String id) {
        var result = super.getRunningStepState(id);
        if (result.isPresent())
            return result;
        return getPersistentStepState(id);
    }

    @Override
    public void addJobState(JobState jobState) {
        super.addJobState(jobState);
        addPersistentJobState(jobState);
    }

    @Override
    public void updateJobState(JobState jobState) {
        updatePersistentJobState(jobState);
    }

    @Override
    public void addStepState(StepState stepState) {
        super.addStepState(stepState);
        addPersistentStepState(stepState);
    }

    @Override
    public void updateStepState(StepState stepState) {
        updatePersistentStepState(stepState);
    }

}
