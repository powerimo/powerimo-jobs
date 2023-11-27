package org.powerimo.jobs.features;

public interface ExecutionFeatureSupport {
    boolean isFeatureEnabled(ExecutionFeature feature);
    void enableFeature(ExecutionFeature feature);
    void disableFeature(ExecutionFeature feature);
}
