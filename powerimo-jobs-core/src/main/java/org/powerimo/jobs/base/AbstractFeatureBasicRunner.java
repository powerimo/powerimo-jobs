package org.powerimo.jobs.base;

import lombok.Getter;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.features.ExecutionFeatureSupport;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractFeatureBasicRunner extends AbstractBasicRunner implements ExecutionFeatureSupport {
    private final List<ExecutionFeature> executionFeatures = new ArrayList<>();

    @Override
    public boolean isFeatureEnabled(ExecutionFeature feature) {
        return executionFeatures.contains(feature);
    }

    @Override
    public void enableFeature(ExecutionFeature feature) {
        if (!executionFeatures.contains(feature))
            executionFeatures.add(feature);
    }

    @Override
    public void disableFeature(ExecutionFeature feature) {
        executionFeatures.remove(feature);
    }

}
