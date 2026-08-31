package com.taskschedule.service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taskschedule.models.entity.JobEntity;

public interface TaskRepository extends JpaRepository<JobEntity, UUID>, TaskRepositoryCustom {
    @Query(value = """
                SELECT *
                FROM jobs
                WHERE status = 'PENDING' AND run_at <= NOW()
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<JobEntity> findNextPendingJob();
}
