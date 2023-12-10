package org.powerimo.jobs.tools;

import org.powerimo.jobs.IdSupport;
import org.powerimo.jobs.JobDescriptor;
import org.powerimo.jobs.StepDescriptor;

public class JobsUtils {
    public static String stepLogPrefix(JobDescriptor jobDescriptor, StepDescriptor stepDescriptor) {
        return "[" + jobDescriptor.getCode() + "][" + stepDescriptor.getCode() + "]";
    }

    public static String extractId(Object o) {
        if (o == null)
            return null;
        if (o instanceof IdSupport) {
            IdSupport idSupport = (IdSupport) o;
            return idSupport.getId();
        }
        return null;
    }

}
