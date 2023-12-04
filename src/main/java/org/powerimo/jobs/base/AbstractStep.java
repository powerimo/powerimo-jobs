package org.powerimo.jobs.base;

import lombok.Getter;
import lombok.Setter;
import org.powerimo.jobs.*;

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

        return doRun();
    }

    protected abstract StepResult doRun();
}

