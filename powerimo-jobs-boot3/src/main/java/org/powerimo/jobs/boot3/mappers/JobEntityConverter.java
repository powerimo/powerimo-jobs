package org.powerimo.jobs.boot3.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.JobState;
import org.powerimo.jobs.boot3.entities.JobEntity;
import org.powerimo.jobs.std.StdJobResult;
import org.powerimo.jobs.std.StdJobState;
import org.springframework.core.convert.converter.Converter;

public class JobEntityConverter implements Converter<JobEntity, JobState> {
    @Getter
    @Setter
    private ObjectMapper objectMapper;

    public JobEntityConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JobEntityConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public StdJobState convert(JobEntity source) {
        return StdJobState.builder()
                .id(source.getId())
                .status(source.getStatus())
                .startedAt(source.getStartedAt())
                .completedAt(source.getCompletedAt())
                .title(source.getTitle())
                .status(source.getStatus())
                .jobResult(
                        StdJobResult.builder()
                                .result(source.getResult())
                                .message(source.getResultMessage())
                        .build())
                .build();
    }
}
