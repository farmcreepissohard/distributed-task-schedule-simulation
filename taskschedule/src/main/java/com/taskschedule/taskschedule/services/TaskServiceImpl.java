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
    public JobEntity updateJob(JobEntity job) {
        if (job == null) {
            throw new IllegalArgumentException("Job is required");
        }
        return repository.save(job);
    }

    @Transactional
    public JobEntity initJob(String jobType, JsonNode payload) {
        if (jobType == null) {
            throw new IllegalArgumentException("Job type is required");
        }
        if (jobType.isEmpty()) {
            throw new IllegalArgumentException("Job type is invalid");
        }
        if (payload == null) {
            throw new IllegalArgumentException("Payload is required");
        }
        return repository.save(new JobEntity(jobType, payload));
    }

    @Transactional
    public int resetZombieJobs() {
        return repository.resetZombieJobs();
    }
}
