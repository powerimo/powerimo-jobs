package org.powerimo.jobs.generators;

import org.powerimo.jobs.IdGenerator;

import java.util.UUID;

public class UuidGenerator implements IdGenerator {

    @Override
    public String getNextId() {
        return UUID.randomUUID().toString();
    }
}
