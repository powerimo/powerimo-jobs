package org.powerimo.jobs.boot2.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.powerimo.jobs.boot2.entities.JobEntity;
import org.powerimo.jobs.JobState;
import org.springframework.core.convert.converter.Converter;

public class JobStateConverter implements Converter<JobState, JobEntity> {
    @Getter
    @Setter
    private ObjectMapper objectMapper;

    public JobStateConverter(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public JobStateConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    public JobEntity convert(JobState source) {
        var params = objectMapper.writeValueAsString(source.getArguments());

        return JobEntity.builder()
                .id(source.getId())
                .title(source.getTitle())
                .code(source.getJobDescriptor().getCode())
                .startedAt(source.getStartedAt())
                .completedAt(source.getCompletedAt())
                .parameters(params)
                .status(source.getStatus())
                .resultMessage(source.getJobResult().getMessage())
                .result(source.getJobResult().getResult())
                .build();
    }
}
