package com.example.trainingt1.dto;

import com.example.trainingt1.entity.TaskStatus;
import lombok.Data;

@Data
public class TaskDtoIn {
    private String title;
    private String description;
    private TaskStatus status;
    private Long userId;
}
