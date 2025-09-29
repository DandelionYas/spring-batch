package myapp.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

  private final JobLauncher jobLauncher;
  private final Job uppercaseJob;
  private final Job invoiceJob;

  @Scheduled(cron = "${batch-app.uppercaseJobCron}")
  public void runUppercaseJob() {
    run("uppercase-job",  uppercaseJob);
  }

  @Scheduled(cron = "${batch-app.invoiceJobCron}")
  public void runInvoiceJob() {
    run("invoice-job", invoiceJob);
  }

  private void run(String jobName, Job job) {
    JobParameters params = new JobParametersBuilder()
        .addString(jobName, LocalDateTime.now().toString())
        .toJobParameters();

    try {
      log.info("Starting job...");
      jobLauncher.run(job, params);
      log.info("Finished job...");

    } catch (JobExecutionAlreadyRunningException e) {
      log.error("Job already running", e);
    } catch (JobRestartException e) {
      log.error("Job restart failed", e);
    } catch (JobInstanceAlreadyCompleteException e) {
      log.error("Job instance already complete", e);
    } catch (JobParametersInvalidException e) {
      log.error("Job parameters invalid", e);
    }
  }
}
