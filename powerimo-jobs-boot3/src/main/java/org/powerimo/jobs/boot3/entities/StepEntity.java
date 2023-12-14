package org.powerimo.jobs.boot3.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Status;

import jakarta.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "powerimo_job_step")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepEntity {
    @Id
    private String id;
    private String jobId;
    private Instant startedAt;
    private Instant completedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String code;
    private int stepOrder;

    @Enumerated(EnumType.STRING)
    private Result result;

    private String resultMessage;
    private Integer countTotal;
    private Integer countError;
}
