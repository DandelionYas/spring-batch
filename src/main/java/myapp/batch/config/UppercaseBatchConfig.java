package myapp.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
public class UppercaseBatchConfig {

    @Bean
    public Job uppercaseJob(JobRepository jobRepository, Step uppercaseStep) {
        return new JobBuilder("uppercase-job", jobRepository)
                .start(uppercaseStep)
                .build();
    }

    @Bean
    public Step uppercaseStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("uppercase-step", jobRepository)
                .<String, String>chunk(2, transactionManager)
                .reader(uppercaseReader())
                .processor(upperCaseProcessor())
                .writer(items -> items.forEach(log::info))
                .build();
    }

    @Bean
    @StepScope // This is required because spring batch recognized consumed stateful reader
    // use a stateless reader like DB instead
    public ListItemReader<String> uppercaseReader() {
        return new ListItemReader<>(List.of("Spring", "Batch", "Demo"));
    }

    @Bean
    public ItemProcessor<String, String> upperCaseProcessor() {
        return String::toUpperCase;
    }
}
