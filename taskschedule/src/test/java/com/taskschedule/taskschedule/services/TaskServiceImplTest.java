package com.taskschedule.taskschedule.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskschedule.taskschedule.models.entity.JobEntity;
import com.taskschedule.taskschedule.models.enums.JobStatusEnum;
import com.taskschedule.taskschedule.repositories.TaskRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    @Nested
    @DisplayName("claimNextJob function test")
    class ClaimNextJobTests {

        @Test
        @DisplayName("success and return null object of job")
        void shouldSuccessAndReturnNull() {
            when(repository.findNextPendingJob()).thenReturn(Optional.ofNullable(null));

            JobEntity job = service.claimNextJob();

            assertNull(job);
            verify(repository, times(1)).findNextPendingJob();
            verify(repository, times(0)).save(any());
        }

        @Test
        @DisplayName("success and return a job")
        void shouldSuccessAndReturnJob() {
            JobEntity testJob = new JobEntity();
            testJob.setId(UUID.randomUUID());
            testJob.setStatus(JobStatusEnum.PENDING);

            when(repository.findNextPendingJob()).thenReturn(Optional.of(testJob));
            when(repository.save(any(JobEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            JobEntity job = service.claimNextJob();

            assertEquals(testJob.getId(), job.getId());
            assertEquals(JobStatusEnum.RUNNING, job.getStatus());
            verify(repository, times(1)).findNextPendingJob();
            verify(repository, times(1)).save(any(JobEntity.class));
        }
    }

    @Nested
    @DisplayName("UpdateJob Tests")
    class UpdateJobTests {

        @Test
        @DisplayName("success and save job in repository")
        void shouldSuccess() {
            JobEntity testJob = new JobEntity();
            testJob.setId(UUID.fromString("0bbac7ef-f0e0-49f3-9a77-9fc943df82de"));
            when(repository.save(any(JobEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            assertEquals("0bbac7ef-f0e0-49f3-9a77-9fc943df82de", service.updateJob(testJob).getId().toString());
            verify(repository, times(1)).save(testJob);
        }

        @Test
        @DisplayName("failed and throw IllegalArgumentException when job is null")
        void shouldFailedWhenNullJob() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.updateJob(null));
            assertEquals("Job is required", exception.getMessage());
            verifyNoInteractions(repository);
        }
    }

    @Nested
    @DisplayName("InitJob Tests")
    class InitJobTests {

        @Test
        @DisplayName("success and save in repository")
        void shouldSuccess() {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payload = objectMapper.readTree("{\"id\": 1, \"name\": \"Test\"}");
            when(repository.save(any(JobEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            JobEntity job = service.initJob("EMAIL", payload);

            assertEquals("EMAIL", job.getJobType());
            assertEquals(payload, job.getPayload());
            assertEquals(JobStatusEnum.PENDING, job.getStatus());
            verify(repository, times(1)).save(any(JobEntity.class));
        }

        @Test
        @DisplayName("failed when job type is null")
        void shouldFailedWhenNullJobType() {
            ObjectMapper objectMapper = new ObjectMapper();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.initJob(null, objectMapper.readTree("{\"id\": 1, \"name\": \"Test\"}")));

            assertEquals("Job type is required", exception.getMessage());
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("failed when job type is empty")
        void shouldFailedWhenEmptyJobType() {
            ObjectMapper objectMapper = new ObjectMapper();
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.initJob("", objectMapper.readTree("{\"id\": 1, \"name\": \"Test\"}")));

            assertEquals("Job type is invalid", exception.getMessage());
            verifyNoInteractions(repository);
        }

        @Test
        @DisplayName("failed when payload is null")
        void shouldFailedWhenNullPayload() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> service.initJob("Email", null));
            assertEquals("Payload is required", exception.getMessage());
            verifyNoInteractions(repository);
        }
    }

}
