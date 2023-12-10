package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.Step;
import org.powerimo.jobs.StepDescriptor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdStepDescriptor implements StepDescriptor {
    private String code;
    private String name;
    private int order = 100;
    private String jobCode;
    private Class<? extends Step> stepClass;
    private boolean onExceptionContinue = true;

    public static StdStepDescriptor of(String code, String name, int order, Class<? extends Step> stepClass, String jobCode) {
        return StdStepDescriptor.builder()
                .code(code)
                .name(name)
                .order(order)
                .stepClass(stepClass)
                .jobCode(jobCode)
                .build();
    }

    @Override
    public boolean getOnExceptionContinue() {
        return onExceptionContinue;
    }
}
