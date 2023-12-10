package org.powerimo.jobs.std.steps;

import lombok.extern.slf4j.Slf4j;
import org.powerimo.jobs.StepResult;
import org.powerimo.jobs.base.AbstractStep;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.std.StdStepResult;

@Slf4j
public class StdJobBannerStep extends AbstractStep {

    @Override
    protected StepResult doRun() {
        final StringBuilder sb = new StringBuilder();
        sb.append("*******************************************\n")
                .append("JOB STARTED\n")
                .append("code: ").append(getContext().getJobDescriptor().getCode()).append("\n")
                .append("name: ").append(getContext().getJobDescriptor().getName()).append("\n")
                .append("id: ").append(getContext().getJobState().getId()).append("\n");

        sb.append("Features enabled: \n");
        getContext().getFeatures().forEach(item -> sb.append("Feature: ").append(item).append("\n"));

        sb.append("\nSteps: \n");
        getContext().getStepDescriptors().forEach(item -> sb.append(item.getOrder())
                .append("|").append(item.getCode()).append(" | ")
                .append(item.getName()).append(" | ")
                .append("class: ").append(item.getClass().getSimpleName()).append(" | ")
                .append("\n"));

        sb.append("*******************************************\n");

        log.info(sb.toString());

        if (isFeatureEnabled(ExecutionFeature.LOG_SYSTEM_OUTPUT)) {
            System.out.println(sb);
        }

        return StdStepResult.success(1,0);
    }

}
