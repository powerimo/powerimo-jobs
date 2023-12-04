package org.powerimo.jobs.generators;

import org.powerimo.jobs.IdGenerator;

public class LocalLongIdGenerator implements IdGenerator {
    private long current = 0L;

    public LocalLongIdGenerator() {
    }

    public LocalLongIdGenerator(long initialValue) {
        current = initialValue;
    }

    @Override
    public String getNextId() {
        current++;
        return String.valueOf(current);
    }
}
