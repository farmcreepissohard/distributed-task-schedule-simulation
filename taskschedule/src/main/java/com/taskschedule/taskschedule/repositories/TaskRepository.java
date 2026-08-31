package com.taskschedule.taskschedule.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.taskschedule.taskschedule.models.entity.JobEntity;

public interface TaskRepository extends JpaRepository<JobEntity, UUID>, TaskRepositoryCustom {
    @Query(value = """
                SELECT *
                FROM jobs
                WHERE status IN ('PENDING', 'RETRY')
                    AND run_at <= NOW()
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<JobEntity> findNextPendingJob();

    @Modifying
    @Query(value = """
                UPDATE jobs
                SET status = CASE
                        WHEN try_times = 1 THEN 'PENDING'
                        ELSE 'RETRY'
                    END,
                    try_times = try_times - 1
                WHERE status = 'RUNNING'
                    AND run_at <= NOW() - INTERVAL '30 minutes'
            """, nativeQuery = true)
    int resetZombieJobs();
}
