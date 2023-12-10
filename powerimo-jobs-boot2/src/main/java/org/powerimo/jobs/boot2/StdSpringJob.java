package org.powerimo.jobs.boot2;

import org.powerimo.jobs.std.StdJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class StdSpringJob extends StdJob {

}
