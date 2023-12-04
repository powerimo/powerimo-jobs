package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.powerimo.jobs.JobResult;
import org.powerimo.jobs.Result;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StdJobResult implements JobResult {
    private Result result;
    private boolean hasErrors;
    private String message;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void setResult(Result value) {
        result = value;
    }

    @Override
    public boolean getHasErrors() {
        return hasErrors;
    }

    @Override
    public void setHasErrors(boolean value) {
        hasErrors = value;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String value) {
        message = value;
    }
}
