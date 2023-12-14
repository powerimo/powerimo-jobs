package org.powerimo.jobs.boot3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.powerimo.jobs.JobState;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.boot3.StdSpringDbStateRepository;
import org.powerimo.jobs.boot3.entities.JobEntity;
import org.powerimo.jobs.boot3.entities.StepEntity;
import org.powerimo.jobs.boot3.mappers.JobEntityConverter;
import org.powerimo.jobs.boot3.mappers.StepEntityConverter;
import org.powerimo.jobs.boot3.repositories.JobRepository;
import org.powerimo.jobs.boot3.repositories.StepRepository;
import org.powerimo.jobs.std.StdJobState;
import org.powerimo.jobs.std.StdStepState;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class StdSpringDbStateRepositoryTest {
    @Mock
    private Converter<StepState, StepEntity> stepStateConverter;

    @Mock
    private Converter<JobState, JobEntity> jobStateConverter;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private JobRepository jobRepository;

    private StdSpringDbStateRepository stateRepository; // Замените YourService на имя вашего сервиса

    private StepState stepStateItem;

    @BeforeEach
    public void init() {
        stepStateItem = StdStepState.builder()
                .jobId("TEST_ID")
                .build();

        stateRepository = new StdSpringDbStateRepository(
                jobRepository,
                stepRepository,
                new JobEntityConverter(),
                new StepEntityConverter(),
                jobStateConverter,
                stepStateConverter
        );
    }


    @Test
    void getPersistentJobState() {

    }

    @Test
    void getPersistentStepState() {
    }

    @Test
    void addPersistentJobState() {
        JobEntity entity = JobEntity.builder().build();
        JobState jobState = new StdJobState();

        when(jobStateConverter.convert(jobState)).thenReturn(entity);
        stateRepository.addJobState(jobState);
        verify(jobRepository).save(entity);
    }

    @Test
    void updatePersistentJobState() {
        JobEntity entity = JobEntity.builder().build();
        JobState jobState = new StdJobState();

        when(jobRepository.findById(jobState.getId())).thenReturn(Optional.of(entity));
        stateRepository.updateJobState(jobState);
        verify(jobRepository).save(entity);
    }

    @Test
    void addPersistentStepState() {
        StepEntity entity = StepEntity.builder().build();

        when(stepStateConverter.convert(stepStateItem)).thenReturn(entity);
        stateRepository.addStepState(stepStateItem);
        verify(stepRepository).save(entity);
    }

    @Test
    void updatePersistentStepState() {
        StepEntity entity = StepEntity.builder().build();

        when(stepStateConverter.convert(stepStateItem)).thenReturn(entity);
        stateRepository.updateStepState(stepStateItem);
        verify(stepRepository).save(entity);
    }
}