package org.powerimo.jobs.boot3.repositories;

import org.powerimo.jobs.boot3.entities.JobEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;

public interface JobRepository extends CrudRepository<JobEntity, String> {
    List<JobEntity> findAllByCodeOrderByStartedAtDesc(String code);
    List<JobEntity> findAllByStartedAtBetween(Instant minStartedAt, Instant maxStartedAt);
    List<JobEntity> findAllByCompletedAtBetween(Instant minStartedAt, Instant maxStartedAt);
}
