package org.powerimo.jobs;

public interface StepResult {
    String getMessage();
    void setMessage(String value);
    Result getResult();
    void setResult(Result value);
    Throwable getCause();
    void setCause(Throwable value);
    long getCountTotal();
    long getCountErrors();
    void setCountTotal(long value);
    void setCountErrors(long value);
}
