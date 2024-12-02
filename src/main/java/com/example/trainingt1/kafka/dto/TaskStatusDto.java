package com.example.trainingt1.kafka.dto;

import com.example.trainingt1.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {
    private Long id;
    private TaskStatus status;
}
