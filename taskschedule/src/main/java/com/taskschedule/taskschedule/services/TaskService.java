package com.taskschedule.taskschedule.services;

import com.taskschedule.taskschedule.models.entity.JobEntity;

import tools.jackson.databind.JsonNode;

public interface TaskService {
    public JobEntity claimNextJob();

    public void updateJob(JobEntity job);

    public void initJob(String jobType, JsonNode payload);

    public int resetZombieJobs();
}
