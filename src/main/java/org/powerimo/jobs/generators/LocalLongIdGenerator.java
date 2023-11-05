package org.powerimo.jobs.generators;

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
