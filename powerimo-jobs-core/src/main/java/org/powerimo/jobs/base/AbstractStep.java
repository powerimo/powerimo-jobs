package org.powerimo.jobs.base;

import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.*;
import org.powerimo.jobs.features.ExecutionFeature;

@Getter
@Setter
public abstract class AbstractStep implements Step, IdSupport {
    private String id;
    private transient JobContext context;
    private transient StepDescriptor stepDescriptor;

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) throws Exception {
        this.context = jobContext;
        this.stepDescriptor = descriptor;

        doBeforeRun();
        return doRun();
    }

    protected void doBeforeRun() {}

    protected abstract StepResult doRun() throws Exception;

    public boolean isFeatureEnabled(ExecutionFeature feature) {
        return  (getContext().getFeatures() != null && getContext().getFeatures().contains(feature));
    }
}

