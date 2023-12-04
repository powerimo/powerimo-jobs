package org.powerimo.jobs.std;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.powerimo.common.utils.Utils;
import org.powerimo.jobs.*;
import org.powerimo.jobs.features.ExecutionFeature;

@Slf4j
public class StdLogJobStatStep implements Step, IdSupport {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public StepResult run(JobContext jobContext, StepDescriptor descriptor) throws Exception {
        var runner = jobContext.getRunner();
        var stateRepository = runner.getConfiguration().getStateRepository();

        if (stateRepository == null) {
            return StdStepResult.error("State repository is not specified");
        }

        var job = jobContext.getJob();
        if (!(job instanceof IdSupport)) {
            return StdStepResult.skipped("Job does not support IDs");
        }

        // get steps states
        final IdSupport jobIdSupport = (IdSupport) jobContext.getJob();
        var stepStateList = stateRepository.getJobStepsStateList(jobIdSupport.getId());

        // log steps
        boolean logSysOutEnabled = jobContext.getFeatures() != null && jobContext.getFeatures().contains(ExecutionFeature.LOG_EXCEPTION_SYSTEM_OUTPUT);
        for (var item: stepStateList) {
            logStepToSlf4j(item);
            if (logSysOutEnabled)
                logStepToSystemOut(item);
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
