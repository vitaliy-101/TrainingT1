package com.example.trainingt1.dto;

import com.example.trainingt1.entity.TaskStatus;
import lombok.Data;

@Data
public class TaskDtoRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private Long userId;
}
