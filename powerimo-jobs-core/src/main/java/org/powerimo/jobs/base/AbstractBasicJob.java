package org.powerimo.jobs.base;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.powerimo.jobs.*;
import org.powerimo.jobs.exceptions.JobException;
import org.powerimo.jobs.exceptions.RunnerException;
import org.powerimo.jobs.features.ExecutionFeature;

import java.util.Objects;
import java.util.Optional;

@Getter
public abstract class AbstractBasicJob implements Job, IdSupport {
    @Setter
    private String id;

    private JobContext context;
    private JobDescriptor jobDescriptor;
    private int currentDescriptorIndex = -1;
    protected StepDescriptor currentStepDescriptor;
    protected StepState currentStepState;


    protected Optional<StepDescriptor> getNextStepDescriptor() {
        int newIndex = currentDescriptorIndex = currentDescriptorIndex + 1;
        var descriptorList = context.getStepDescriptors();

        if (newIndex >= descriptorList.size()) {
            return Optional.empty();
        }

        currentDescriptorIndex = newIndex;
        currentStepDescriptor = descriptorList.get(currentDescriptorIndex);
        return Optional.of(currentStepDescriptor);
    }

    @Override
    public void run(JobContext context, JobDescriptor descriptor) throws Exception {
        this.context = context;
        this.jobDescriptor = descriptor;

        while (getNextStepDescriptor().isPresent()) {
            var step = createStep(currentStepDescriptor);
            var stepState = createStepState(context, step, currentStepDescriptor);
            notifyStepStateCreated(stepState);
            updateContextByStepState(stepState);
            updateCurrentStep(stepState);
            var stepResult = executeStep(step, stepState);

            if (stepResult == null) {
                throw new JobException("executeStep returns null result");
            }

            stepState.setStepResult(stepResult);
            afterStepCompleted(stepState);

            if (Objects.equals(stepResult.getResult(), Result.ERROR) && !currentStepDescriptor.getOnExceptionContinue()) {
                onStepError(stepState, stepResult);
                break;
            }
        }

        // prepare JobResult object
        final JobResult result = prepareJobResult();
        context.getJobState().setJobResult(result);

        afterJobCompletion();

        // report to StateChange receiver about completion
        notifyJobResult();
    }

    protected Step createStep(StepDescriptor descriptor) throws RunnerException {
        var cls = descriptor.getStepClass();
        if (cls == null) {
            throw new JobException("Class is not specified in the descriptor: " + descriptor.getJobCode() + ":" + descriptor.getCode());
        }

        try {
            var instance = descriptor.getStepClass().getDeclaredConstructor().newInstance();
            if (instance instanceof IdSupport) {
                if (context.getRunner().getConfiguration().getStepIdGenerator() != null) {
                    IdSupport idSupport = (IdSupport) instance;
                    var stepId = context.getRunner().getConfiguration().getStepIdGenerator().getNextId();
                    idSupport.setId(stepId);
                }
            }
            return instance;
        } catch (Exception ex) {
            throw new JobException("Exception on creating step", ex);
        }
    }

    protected abstract StepState createStepState(JobContext jobContext, Step step, StepDescriptor descriptor);

    protected StepResult executeStep(@NonNull Step step, @NonNull StepState stepState) {
        StepResult result;
        try {
            result = step.run(context, stepState.getStepDescriptor());
        } catch (Exception ex) {
            result = createResultOnException(stepState, ex);
        }

        return result;
    }

    protected void notifyStepStateCreated(StepState stepState) {
        if (getContext().getStateChangeReceiver() != null) {
            getContext().getStateChangeReceiver().stepCreated(getContext().getJobState(), stepState);
        }
    }

    public boolean isFeatureEnabled(ExecutionFeature feature) {
        return context.getFeatures().contains(feature);
    }

    public void afterStepCompleted(StepState stepState) {}

    protected abstract StepResult createResultOnException(StepState stepState, Exception exception);
    protected abstract JobResult prepareJobResult();
    protected abstract void notifyJobResult();
    protected abstract void afterJobCompletion();
    protected abstract void updateContextByStepState(StepState stepState);
    protected abstract void updateCurrentStep(StepState stepState);
    protected void onStepError(StepState stepState, StepResult stepResult) {};
}
