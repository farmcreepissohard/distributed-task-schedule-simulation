package com.taskschedule.taskschedule.models.dto;

import lombok.Data;
import tools.jackson.databind.JsonNode;

@Data
public class JobDto {
    private String jobType;
    private JsonNode payload;
}
