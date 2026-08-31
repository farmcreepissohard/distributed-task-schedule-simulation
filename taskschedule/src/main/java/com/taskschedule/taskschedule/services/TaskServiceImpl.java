package com.taskschedule.taskschedule.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.taskschedule.taskschedule.models.entity.JobEntity;
import com.taskschedule.taskschedule.models.enums.JobStatusEnum;
import com.taskschedule.taskschedule.repositories.TaskRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Transactional
    public JobEntity claimNextJob() {
        Optional<JobEntity> optionalJob = repository.findNextPendingJob();
        if (optionalJob.isPresent()) {
            JobEntity job = optionalJob.get();
            job.setStatus(JobStatusEnum.RUNNING);
            return repository.save(job);
        }
        return null;
    }

    @Transactional
    public void updateJob(JobEntity job) {
        repository.save(job);
    }

    @Transactional
    public void initJob(String jobType, JsonNode payload) {
        repository.save(new JobEntity(jobType, payload));
    }

}
