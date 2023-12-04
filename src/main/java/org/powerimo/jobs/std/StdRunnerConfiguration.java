package org.powerimo.jobs.std;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.DescriptorRepository;
import org.powerimo.jobs.IdGenerator;
import org.powerimo.jobs.RunnerConfiguration;
import org.powerimo.jobs.StateRepository;
import org.powerimo.jobs.generators.UuidGenerator;

@Data
@NoArgsConstructor
public class StdRunnerConfiguration implements RunnerConfiguration {
    private DescriptorRepository descriptorRepository;
    private StateRepository stateRepository;
    private IdGenerator jobIdGenerator;
    private IdGenerator stepIdGenerator;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DescriptorRepository descriptorRepository;
        private StateRepository stateRepository;
        private IdGenerator jobIdGenerator;
        private IdGenerator stepIdGenerator;
        private boolean createMissing = true;

        public Builder descriptorRepository(DescriptorRepository repository) {
            this.descriptorRepository = repository;
            return this;
        }

        public Builder stateRepository(StateRepository stateRepository) {
            this.stateRepository = stateRepository;
            return this;
        }

        public Builder jobIdGenerator(IdGenerator generator) {
            this.jobIdGenerator = generator;
            return this;
        }

        public Builder stepIdGenerator(IdGenerator generator) {
            this.stepIdGenerator = generator;
            return this;
        }

        public Builder createMissing(boolean value) {
            this.createMissing = value;
            return this;
        }

        protected void doCreateMissing() {
            if (descriptorRepository == null)
                descriptorRepository = new StdDescriptorRepository();
            if (stateRepository == null)
                stateRepository = new StdInMemoryStateRepository();
            if (jobIdGenerator == null)
                jobIdGenerator = new UuidGenerator();
            if (stepIdGenerator == null)
                stepIdGenerator = new UuidGenerator();
        }

        public StdRunnerConfiguration build() {
            final StdRunnerConfiguration configuration = new StdRunnerConfiguration();
            if (createMissing) {
                doCreateMissing();
            }
            configuration.descriptorRepository = descriptorRepository;
            configuration.stateRepository = stateRepository;
            configuration.jobIdGenerator = jobIdGenerator;
            configuration.stepIdGenerator = stepIdGenerator;
            return configuration;
        }
    }

}
