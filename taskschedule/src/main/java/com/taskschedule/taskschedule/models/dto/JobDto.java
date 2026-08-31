package com.taskschedule.taskschedule.models.dto;

import lombok.Data;
import tools.jackson.databind.JsonNode;

@Data
public class JobDto {
    private JsonNode payload;
}
