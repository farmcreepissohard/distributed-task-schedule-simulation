package com.taskschedule.taskschedule.models.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.taskschedule.taskschedule.models.enums.JobStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Entity
@Table(name = "jobs")
@Getter
@NoArgsConstructor
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "job_type", length = 50)
    private String jobType;

    @Setter
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private JobStatusEnum status = JobStatusEnum.PENDING;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Setter
    @Column(name = "try_times")
    private Integer tryTimes = 0;

    @Column(name = "max_retries")
    private Integer maxRetries = 3;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Setter
    @Column(name = "run_at", nullable = false)
    private OffsetDateTime runAt;

    public JobEntity(String jobType, JsonNode payload) {
        this.jobType = jobType;
        this.payload = payload;
        this.runAt = OffsetDateTime.now().plusMinutes(5);
    }
}
