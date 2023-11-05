package org.powerimo.jobs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDescriptor {
    private String code;
    private String name;
    private Class<? extends Job> jobClass;

    public static JobDescriptor create(String code, String name, Class<? extends Job> jobClass) {
        return JobDescriptor.builder()
                .code(code)
                .name(name)
                .jobClass(jobClass)
                .build();
    }
}
