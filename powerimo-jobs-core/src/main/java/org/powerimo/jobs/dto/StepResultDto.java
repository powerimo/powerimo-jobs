package org.powerimo.jobs.dto;

import lombok.Data;
import org.powerimo.jobs.Result;
import org.powerimo.jobs.StepResult;

@Data
public class StepResultDto {
    private Result result;
    private String message;

    public StepResultDto(StepResult stepResult) {
        result = stepResult.getResult();
        message = stepResult.getMessage();
    }

}
