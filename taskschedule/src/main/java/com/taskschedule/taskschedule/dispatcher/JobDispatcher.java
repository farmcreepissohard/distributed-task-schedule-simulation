package com.taskschedule.taskschedule.dispatcher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.taskschedule.taskschedule.models.entity.JobEntity;
import com.taskschedule.taskschedule.services.TaskProcessor;
import com.taskschedule.taskschedule.services.TaskService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobDispatcher {

    private final TaskService taskService;
    private final TaskProcessor taskProcessor;

    @Scheduled(fixedDelay = 1000)
    public void dispatch() {
        while (true) {
            JobEntity claimedJob = taskService.claimNextJob();

            if (claimedJob == null) {
                break;
            }
            taskProcessor.process(claimedJob);
        }
    }

}
