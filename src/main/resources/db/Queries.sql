-- Job instances
SELECT job_instance_id, job_name FROM batch_job_instance;

-- Job Executions
SELECT job_execution_id, status, start_time, end_time
    FROM batch_job_execution
ORDER BY create_time DESC;

-- Batch Step Execution
SELECT step_name, read_count, write_count, status
    FROM batch_step_execution
WHERE job_execution_id = 1;

select * from batch_job_instance;
select * from batch_job_execution order by create_time desc;
select * from batch_step_execution;
select * from batch_job_execution_params;



-- Clear all data in batch tables
delete from batch_job_execution_params;
delete from batch_job_execution_context;
delete from batch_step_execution_context;
delete from batch_step_execution;
delete from batch_job_execution;
delete from batch_job_instance;