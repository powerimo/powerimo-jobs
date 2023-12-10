package org.powerimo.jobs.std;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.powerimo.jobs.DescriptorRepository;
import org.powerimo.jobs.IdGenerator;
import org.powerimo.jobs.RunnerConfiguration;
import org.powerimo.jobs.StateRepository;
import org.powerimo.jobs.features.ExecutionFeature;
import org.powerimo.jobs.generators.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class StdRunnerConfiguration implements RunnerConfiguration {
    private DescriptorRepository descriptorRepository;
    private StateRepository stateRepository;
    private IdGenerator jobIdGenerator;
    private IdGenerator stepIdGenerator;
    private List<ExecutionFeature> executionFeatures;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DescriptorRepository descriptorRepository;
        private StateRepository stateRepository;
        private IdGenerator jobIdGenerator;
        private IdGenerator stepIdGenerator;
        private boolean createMissing = true;
        private List<ExecutionFeature> executionFeatureList;

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

        public Builder executionFeatures(List<ExecutionFeature> features) {
            this.executionFeatureList = features;
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
            if (executionFeatureList == null) {
                executionFeatureList = new ArrayList<>();
                executionFeatureList.add(ExecutionFeature.LOG_EXCEPTION);
                executionFeatureList.add(ExecutionFeature.LOG_JOB_COMPLETED);
                executionFeatureList.add(ExecutionFeature.LOG_STEP_COMPLETED);
            }
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
            configuration.executionFeatures = executionFeatureList;
            return configuration;
        }
    }

}
