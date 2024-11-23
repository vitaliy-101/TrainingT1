package com.example.trainingt1.dto;

import lombok.Data;

@Data
public class TaskDtoIn {
    private String title;
    private String description;
    private Long userId;
}
