package org.powerimo.jobs;

public interface StepDescriptor {
    String getCode();
    String getName();
    int getOrder();
    String getJobCode();
    Class<? extends Step> getStepClass();
    boolean getOnExceptionContinue();
}
