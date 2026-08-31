package com.taskschedule.taskschedule.services;

import java.time.OffsetDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.taskschedule.taskschedule.models.entity.JobEntity;
import com.taskschedule.taskschedule.models.enums.JobStatusEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessor {
    private final TaskService service;

    @Async
    public void process(JobEntity job) {
        log.info("Start {} job: {}", job.getJobType(), job.getId());

        try {
            Integer tryTimes = job.getTryTimes();
            job.setTryTimes(tryTimes + 1);
            job.setStatus(JobStatusEnum.RUNNING);

            log.info("{} retry {} times", job.getId(), job.getTryTimes());

            Thread.sleep(2000);
            if (Math.random() > 0.5) {
                throw new RuntimeException("Failed in running");
            }

            job.setStatus(JobStatusEnum.DONE);
        } catch (Exception e) {
            log.error("Failed in running");

            if (job.getTryTimes() >= job.getMaxRetries()) {
                log.warn("{} is dead", job.getId());
                job.setStatus(JobStatusEnum.ERROR);
            } else {
                log.warn("{} retry ", job.getId());
                job.setStatus(JobStatusEnum.RETRY);
                job.setRunAt(OffsetDateTime.now().plusMinutes((long) Math.pow(2, job.getTryTimes())));
            }
        } finally {
            service.updateJob(job);
        }
    }
}
