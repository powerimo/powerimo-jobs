package org.powerimo.jobs.boot2;

import org.powerimo.jobs.DescriptorRepository;
import org.powerimo.jobs.Runner;
import org.powerimo.jobs.RunnerConfiguration;
import org.powerimo.jobs.StateRepository;
import org.powerimo.jobs.boot2.mappers.JobEntityConverter;
import org.powerimo.jobs.boot2.mappers.JobStateConverter;
import org.powerimo.jobs.boot2.mappers.StepEntityConverter;
import org.powerimo.jobs.boot2.mappers.StepStateConverter;
import org.powerimo.jobs.boot2.repositories.JobRepository;
import org.powerimo.jobs.boot2.repositories.StepRepository;
import org.powerimo.jobs.std.StdDescriptorRepository;
import org.powerimo.jobs.std.StdRunnerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;

@Configuration
@ConditionalOnClass({EntityManager.class, Repository.class})
@EnableJpaRepositories(basePackages = {"org.powerimo.jobs.boot2.repositories"})
@EntityScan(basePackages = {"org.powerimo.jobs.boot2.entities"})
public class PowerimoJobsBoot2AutoConfiguration {
    private JobRepository jpaJobRepository;
    private StepRepository jpaStepRepository;

    @Autowired
    public void setJpaJobRepository(JobRepository jpaJobRepository) {
        this.jpaJobRepository = jpaJobRepository;
    }

    @Autowired
    public void setJpaStepRepository(StepRepository jpaStepRepository) {
        this.jpaStepRepository = jpaStepRepository;
    }

    @ConditionalOnMissingBean
    @Bean
    public StateRepository stateRepository() {
        var jonEntityConverter = new JobEntityConverter();
        var jobStateConverter = new JobStateConverter();
        var stepEntityConverter = new StepEntityConverter();
        var stepStateConverter = new StepStateConverter();
        return new StdSpringDbStateRepository(jpaJobRepository, jpaStepRepository, jonEntityConverter, stepEntityConverter, jobStateConverter, stepStateConverter);
    }

    @ConditionalOnMissingBean
    @Bean
    public DescriptorRepository descriptorRepository() {
        return new StdDescriptorRepository();
    }

    @ConditionalOnMissingBean
    @Bean
    public RunnerConfiguration runnerConfiguration(StateRepository stateRepository, DescriptorRepository descriptorRepository) {
        return StdRunnerConfiguration.builder()
                .createMissing(true)
                .descriptorRepository(descriptorRepository)
                .stateRepository(stateRepository)
                .build();
    }

    @ConditionalOnMissingBean
    @Bean
    public Runner runner(RunnerConfiguration configuration, ApplicationContext applicationContext) {
        var runner = new StdSpringRunner(applicationContext);
        runner.setConfiguration(configuration);
        return runner;
    }

}
