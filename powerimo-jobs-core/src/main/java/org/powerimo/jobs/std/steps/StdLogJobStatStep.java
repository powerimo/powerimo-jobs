package org.powerimo.jobs.std.steps;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.common.utils.Utils;
import org.powerimo.jobs.IdSupport;
import org.powerimo.jobs.StepResult;
import org.powerimo.jobs.StepState;
import org.powerimo.jobs.base.AbstractStep;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.std.StdStepResult;

@Slf4j
public class StdLogJobStatStep extends AbstractStep {

    @Override
    protected StepResult doRun() {
        var runner = getContext().getRunner();
        var stateRepository = runner.getConfiguration().getStateRepository();

        if (stateRepository == null) {
            return StdStepResult.error("State repository is not specified");
        }

        var job = getContext().getJob();
        if (!(job instanceof IdSupport)) {
            return StdStepResult.skipped("Job does not support IDs");
        }

        // get steps states
        final IdSupport jobIdSupport = (IdSupport) job;
        var stepStateList = stateRepository.getJobStepsStateList(jobIdSupport.getId());

        // log steps
        boolean logSysOutEnabled = isFeatureEnabled(ExecutionFeature.LOG_SYSTEM_OUTPUT);

        log.info("===========================================\n");
        log.info("Job statistics\n");
        if (logSysOutEnabled) {
            System.out.println("===========================================\n");
            System.out.println("Job statistics\n");
        }

        for (var item: stepStateList) {
            logStepToSlf4j(item);
            if (logSysOutEnabled)
                logStepToSystemOut(item);
        }

        log.info("===========================================\n");
        if (logSysOutEnabled) {
            System.out.println("===========================================\n");
        }

        return StdStepResult.success(stepStateList.size(), 0);
    }

    public void logStepToSlf4j(@NonNull StepState stepState) {
        var stepResult = stepState.getStepResult();
        if (stepResult == null) {
            log.error("step does not has result: {}", stepState);
            return;
        }

        var s = formatString(stepState);
        log.info(s);
    }

    public void logStepToSystemOut(@NonNull StepState stepState) {
        var s = formatString(stepState);
        System.out.println(s);
    }

    public String formatString(StepState stepState) {
        var stepResult = stepState.getStepResult();
        String result;
        if (stepResult == null) {
            result = Utils.slf4jFormat("step does not has result: {}", stepState);
            return result;
        }

        result = Utils.slf4jFormat("{} | {} | {} | {}",
                stepState.getId(),
                stepState.getStepDescriptor().getName(),
                stepState.getStatus(),
                stepResult.getResult() );
        return result;
    }

}
