package org.powerimo.jobs.boot3.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.powerimo.jobs.*;
import org.powerimo.jobs.boot3.entities.JobEntity;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class JobStateConverter implements Converter<JobState, JobEntity> {
    @Getter
    @Setter
    private ObjectMapper objectMapper;

    public JobStateConverter(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public JobStateConverter() {
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @SneakyThrows
    @Override
    public JobEntity convert(JobState source) {
        var parameterText = serializeParameters(source.getArguments());

        return JobEntity.builder()
                .id(source.getId())
                .title(source.getTitle())
                .code(source.getJobDescriptor().getCode())
                .startedAt(source.getStartedAt())
                .completedAt(source.getCompletedAt())
                .status(source.getStatus())
                .resultMessage(source.getJobResult().getMessage())
                .result(source.getJobResult().getResult())
                .parameters(parameterText)
                .build();
    }

    private String serializeParameters(List<Object> list) throws JsonProcessingException {
        var filteredList = list.stream()
                .filter(JobStateConverter::isSerializableParameter)
                .collect(Collectors.toList());
        return objectMapper.writeValueAsString(filteredList);
    }

    private static boolean isSerializableParameter(Object o) {
        return !o.getClass().isAssignableFrom(JobContext.class)
                && !(o.getClass().isAssignableFrom(JobState.class)
                && !(o.getClass().isAssignableFrom(StepState.class))
                && !o.getClass().isAssignableFrom(Job.class)
                && !o.getClass().isAssignableFrom(Step.class));
    }
}
