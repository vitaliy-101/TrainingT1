package com.example.trainingt1.mapper;


import com.example.trainingt1.dto.TaskDto;
import com.example.trainingt1.dto.TaskDtoIn;
import com.example.trainingt1.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    Task convertToTaskEntity(TaskDtoIn taskDto);

    TaskDto convertTaskToDto(Task task);

    List<TaskDto> convertTaskListToDto(List<Task> tasks);
}
