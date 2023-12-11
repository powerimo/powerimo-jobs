package org.powerimo.jobs.boot2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.Status;
import org.powerimo.jobs.boot2.entities.JobEntity;
import org.powerimo.jobs.boot2.entities.StepEntity;
import org.powerimo.jobs.boot2.repositories.JobRepository;
import org.powerimo.jobs.boot2.repositories.StepRepository;
import org.powerimo.jobs.JobState;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.base.AbstractPersistentStateRepository;
import org.powerimo.jobs.exceptions.StateRepositoryException;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class StdSpringDbStateRepository extends AbstractPersistentStateRepository {
    private final JobRepository jobRepository;
    private final StepRepository stepRepository;
    private final Converter<JobEntity, JobState> jobEntityConverter;
    private final Converter<StepEntity, StepState> stepEntityConverter;
    private final Converter<JobState, JobEntity> jobStateConverter;
    private final Converter<StepState, StepEntity> stepStateConverter;

    @Override
    public void updateJobState(JobState jobState) {
        super.updateJobState(jobState);

        if (jobState.getStatus() == Status.COMPLETED) {
            getRunningJobStateList().remove(jobState);
        }
    }

    @Override
    public Optional<JobState> getPersistentJobState(String id) {
        var entity = jobRepository.findById(id).orElse(null);
        if (entity == null)
            return Optional.empty();
        return Optional.ofNullable(jobEntityConverter.convert(entity));
    }

    @Override
    public Optional<StepState> getPersistentStepState(String id) {
        var entity = stepRepository.findById(id).orElse(null);
        if (entity == null)
            return Optional.empty();
        return Optional.ofNullable(stepEntityConverter.convert(entity));
    }

    @Override
    public void addPersistentJobState(JobState jobState) {
        var entity = jobStateConverter.convert(jobState);
        if (entity == null) {
            throw new StateRepositoryException("Converter returns empty entity");
        }
        jobRepository.save(entity);
    }

    @Override
    public void updatePersistentJobState(JobState jobState) {
        var entityOpt = jobRepository.findById(jobState.getId());
        if (entityOpt.isEmpty()) {
            throw new StateRepositoryException("Converter returns empty entity");
        }
        var entity = entityOpt.get();
        entity.setResult(jobState.getJobResult().getResult());
        entity.setResultMessage(jobState.getJobResult().getMessage());
        entity.setCompletedAt(jobState.getCompletedAt());
        entity.setStatus(jobState.getStatus());
        jobRepository.save(entity);
    }

    @Override
    public void addPersistentStepState(StepState stepState) {
        var entity = stepStateConverter.convert(stepState);
        if (entity == null) {
            throw new StateRepositoryException("Converter returns empty entity");
        }
        stepRepository.save(entity);
    }

    @Override
    public void updatePersistentStepState(StepState stepState) {
        var entity = stepStateConverter.convert(stepState);
        if (entity == null) {
            throw new StateRepositoryException("Converter returns empty entity");
        }
        stepRepository.save(entity);
    }
}
