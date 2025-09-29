package myapp.batch.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class InvoiceBatchConfig {

    @Bean
    public Job invoiceJob(JobRepository jobRepository, Step invoiceStep) {
        return new JobBuilder("invoice-job", jobRepository)
                .start(invoiceStep)
                .build();
    }

    @Bean
    public Step invoiceStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("invoice-step", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(invoiceItemReader())
                .processor(item -> "Processed-" + item)
                .writer(items -> items.forEach(log::info))
                .build();
    }

    @Bean
    @StepScope
    public static ListItemReader<String> invoiceItemReader() {
        return new ListItemReader<>(List.of("Invoice1", "Invoice2", "Invoice3"));
    }
}
