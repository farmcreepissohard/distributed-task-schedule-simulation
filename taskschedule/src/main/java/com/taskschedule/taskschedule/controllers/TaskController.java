package com.taskschedule.taskschedule.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskschedule.taskschedule.models.dto.JobDto;
import com.taskschedule.taskschedule.services.TaskService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TaskController {
    private final TaskService service;

    @PostMapping("/jobs")
    public ResponseEntity<String> createJob(@RequestBody JobDto dto) {
        service.initJob(dto.getJobType(), dto.getPayload());

        return ResponseEntity.status(HttpStatus.CREATED).body("Job created");
    }

}
