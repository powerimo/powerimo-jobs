package org.powerimo.jobs.std;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;

@Getter
@Slf4j
public class StdJob implements Job {
    private JobContext context;
    private int currentDescriptorIndex = 0;

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
            step = cls.getDeclaredConstructor().newInstance();
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

}
