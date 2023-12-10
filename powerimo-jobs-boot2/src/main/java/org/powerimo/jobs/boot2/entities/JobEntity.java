package org.powerimo.jobs.boot2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.Status;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "powerimo_job")
public class JobEntity {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant startedAt;
    private Instant completedAt;
    private String code;
    private String title;

    @Enumerated
    private Result result;
    private String resultMessage;

    @Column(name = "job_parameters")
    private String parameters;

    @Column(name = "job_parameters_class")
    private String parametersClass;
}
