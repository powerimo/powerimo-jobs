package org.powerimo.jobs.std;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.tools.JobsUtils;
import org.powerimo.jobs.tools.StepResults;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class StdJob implements Job, IdSupport {
    @Getter
    private JobContext context;

    @Getter
    private JobDescriptor descriptor;
    private int currentDescriptorIndex = 0;
    private final StepResults stepResults = new StepResults();

    private String id;

    /**
     * Run the job
     * @param context the context with parameters
     * @param descriptor the descriptor used for job creation
     * @throws Exception any exception
     */
    @Override
    public void run(@NonNull JobContext context, @NonNull JobDescriptor descriptor) throws Exception {
        this.context = context;
        this.descriptor = descriptor;

        for (; currentDescriptorIndex < context.getStepDescriptors().size(); currentDescriptorIndex++) {
            var stepDescriptor = context.getStepDescriptors().get(currentDescriptorIndex);
            var stepResult = executeStep(stepDescriptor);
            stepResults.add(stepResult);

            if (stepResult.getResult() == Result.ERROR) {
                if (!stepDescriptor.getOnExceptionContinue()) {
                    break;
                }
            }
        }

        // prepare JobResult object
        final JobResult result = prepareJobResult();

        // report to Runner about completion
        getContext().getRunner().onJobCompleted(this, result);
    }

    /**
     * Create and execute a step and return the result object
     * @param descriptor The descriptor, which contains the class has to be used for the step creation
     * @return StepResult object
     */
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
            result = step.run(context, descriptor);
        } catch (Exception ex) {
            result = new StepResult();
            result.setResult(Result.ERROR);
            result.setCause(ex);

            if (isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION)) {
                final String prefix = JobsUtils.stepLogPrefix(getContext().getJobDescriptor(), descriptor);
                log.error("{} Exception on execute step", prefix, ex);
            }
            if (isFeatureEnabled(ExecutionFeature.LOG_EXCEPTION_OUTPUT)) {
                final String prefix = JobsUtils.stepLogPrefix(getContext().getJobDescriptor(), descriptor);
                System.out.println(prefix + " Exception on execute step");
                ex.printStackTrace();
            }
        }

        if (result == null) {
            result = new StepResult();
            result.setMessage("The step did not return any result");
            result.setResult(Result.ERROR);
        }

        // enhance the result
        result.setJobId(getId());
        result.setStepId(JobsUtils.extractId(step));
        result.setStepDescriptor(descriptor);
        result.setStepId(JobsUtils.extractId(step));

        getContext().getRunner().onStepComplete(step, result);

        return result;
    }

    protected Step createStep(StepDescriptor descriptor) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return descriptor.getStepClass().getDeclaredConstructor().newInstance();
    }

    protected JobResult prepareJobResult() {
        return JobResult.builder()
                .result(stepResults.hasErrors() ? Result.ERROR : Result.SUCCESS)
                .message(stepResults.hasErrors() ? stepResults.collectErrorMessage() : null)
                .hasErrors(stepResults.hasErrors())
                .build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    protected boolean isFeatureEnabled(ExecutionFeature feature) {
        return context.getFeatures().contains(feature);
    }

}
