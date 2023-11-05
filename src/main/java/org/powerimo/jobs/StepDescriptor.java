package org.powerimo.jobs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDescriptor {
    private String code;
    private String name;
    private int order = 100;
    private String jobCode;
    private Class<? extends Step> stepClass;
    private boolean onExceptionContinue = true;

    public static StepDescriptor of(String code, String name, int order, Class<? extends Step> stepClass, String jobCode) {
        return StepDescriptor.builder()
                .code(code)
                .name(name)
                .order(order)
                .stepClass(stepClass)
                .jobCode(jobCode)
                .build();
    }

}
