package org.powerimo.jobs.boot3.repositories;

import org.powerimo.jobs.Status;
import org.powerimo.jobs.boot3.entities.StepEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StepRepository extends CrudRepository<StepEntity, String> {
    List<StepEntity> findAllByJobIdOrderByStartedAt(String jobId);
    List<StepEntity> findAllByJobIdOrderByStartedAtDesc(String jobId);
    Optional<StepEntity> findFirstByJobIdAndStatus(String jobId, Status status);
}
