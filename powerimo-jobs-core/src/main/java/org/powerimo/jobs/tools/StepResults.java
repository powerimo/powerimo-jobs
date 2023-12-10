package org.powerimo.jobs.tools;

import org.powerimo.jobs.Result;
import org.powerimo.jobs.StepResult;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StepResults extends ArrayList<StepResult> {

    public boolean hasErrors() {
        return stream()
                .anyMatch(item -> item.getResult() == Result.ERROR);
    }

    public String collectErrorMessage() {
        return this.stream()
                .filter(item -> item.getResult() == Result.ERROR)
                .map(item -> extractErrorMessage(item) + "\n")
                .collect(Collectors.joining());
    }

    public String extractErrorMessage(StepResult stepResult) {
        if (stepResult.getMessage() != null)
            return stepResult.getMessage();
        if (stepResult.getCause() != null)
            return stepResult.getCause().getMessage();
        return "";
    }

}
