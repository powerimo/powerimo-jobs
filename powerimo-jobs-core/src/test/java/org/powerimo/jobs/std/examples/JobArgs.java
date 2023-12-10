package org.powerimo.jobs.std.examples;

import lombok.Data;

import java.time.Instant;

@Data
public class JobArgs {
    private String param1;
    private int param2;
    private Instant param3 = Instant.now();
}
