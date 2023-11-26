package org.powerimo.jobs.std;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;

import java.lang.reflect.InvocationTargetException;


@Slf4j
public class StdJob implements Job, IdSupport {
    private JobContext context;
    private int currentDescriptorIndex = 0;
    private String id;

    @Override
    public void run(@NonNull JobContext context) throws Exception {
        this.context = context;
        boolean hasErrors = false;

        for (; currentDescriptorIndex < context.getStepDescriptors().size(); currentDescriptorIndex++) {
            var descriptor = context.getStepDescriptors().get(currentDescriptorIndex);
            var stepResult = executeStep(descriptor);

            if (stepResult.getResult() == Result.ERROR) {
                hasErrors = true;
                if (!descriptor.isOnExceptionContinue()) {
                    break;
                }
            }
        }

        final JobResult result = JobResult.builder()
                .result(hasErrors ? Result.ERROR : Result.SUCCESS)
                .message(hasErrors ? "Exception when execute step: " + context.getStepDescriptors().get(currentDescriptorIndex).getCode() : null)
                .hasErrors(hasErrors)
                .build();

        getContext().getRunner().onJobCompleted(this, result);
    }

    protected StepResult executeStep(StepDescriptor descriptor) {
        var cls = descriptor.getStepClass();
        if (cls == null) {
            throw new JobException("Class is not specified in the descriptor: " + descriptor.getJobCode() + ":" + descriptor.getCode());
        }

        Step step;
        try {
            step = createStep(descriptor);
            getContext().getRunner().onStepCreated(step);
        } catch (Exception ex) {
            throw new JobException("Exception on creating step instance", ex);
        }

        StepResult result;
        try {
            result = step.run(context);
        } catch (Exception ex) {
            result = new StepResult();
            result.setResult(Result.ERROR);
            result.setCause(ex);
        }

        getContext().getRunner().onStepComplete(step, result);

        return result;
    }

    protected Step createStep(StepDescriptor descriptor) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return descriptor.getStepClass().getDeclaredConstructor().newInstance();
    }

    protected JobContext getContext() {
        return context;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
