package org.powerimo.jobs;

public interface ParametersSerializer {
    Object serialize(Object o);
    Object deserialize(Object o);
}
