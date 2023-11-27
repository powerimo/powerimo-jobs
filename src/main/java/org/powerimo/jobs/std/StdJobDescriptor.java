package org.powerimo.jobs.std;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.Job;
import org.powerimo.jobs.JobDescriptor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StdJobDescriptor implements JobDescriptor {
    private String code;
    private String name;
    private Class<? extends Job> jobClass;

    public static StdJobDescriptor create(String code, String name, Class<? extends Job> jobClass) {
        return StdJobDescriptor.builder()
                .code(code)
                .name(name)
                .jobClass(jobClass)
                .build();
    }
}
