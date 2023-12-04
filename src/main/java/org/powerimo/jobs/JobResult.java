package org.powerimo.jobs;

public interface JobResult {
    Result getResult();
    void setResult(Result value);
    boolean getHasErrors();
    void setHasErrors(boolean value);
    String getMessage();
    void setMessage(String value);
}
