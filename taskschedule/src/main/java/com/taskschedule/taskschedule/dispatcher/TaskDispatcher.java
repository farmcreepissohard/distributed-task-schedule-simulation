package com.taskschedule.taskschedule.dispatcher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.taskschedule.taskschedule.models.entity.JobEntity;
import com.taskschedule.taskschedule.services.TaskProcessor;
import com.taskschedule.taskschedule.services.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDispatcher {

    private final TaskService service;
    private final TaskProcessor processor;

    @Scheduled(fixedDelay = 1000)
    public void dispatch() {
        while (true) {
            JobEntity claimedJob = service.claimNextJob();

            if (claimedJob == null) {
                break;
            }
            processor.process(claimedJob);
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void resetZombie() {
        int recoveredJobs = service.resetZombieJobs();
        if (recoveredJobs > 0) {
            log.info("Reset {} zombie jobs", recoveredJobs);
        }
    }

}
