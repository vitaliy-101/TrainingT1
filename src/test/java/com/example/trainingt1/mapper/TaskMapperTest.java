package com.example.trainingt1.mapper;

import com.example.trainingt1.dto.TaskDto;
import com.example.trainingt1.dto.TaskDtoRequest;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    @DisplayName("Тест маппинга из task dto в task entity")
    void testConvertToTaskEntity() {
        TaskDtoRequest taskDto = new TaskDtoRequest();
        taskDto.setTitle("test_title");
        taskDto.setDescription("test_description");
        taskDto.setUserId(1L);
        taskDto.setStatus(TaskStatus.OPENED);
        Task task = taskMapper.convertToTaskEntity(taskDto);
        Assertions.assertNotNull(task);
        Assertions.assertEquals(taskDto.getTitle(), task.getTitle());
        Assertions.assertEquals(taskDto.getDescription(), task.getDescription());
        Assertions.assertEquals(taskDto.getUserId(), task.getUserId());
        Assertions.assertEquals(taskDto.getStatus(), task.getStatus());
    }

    @Test
    @DisplayName("Тест маппинга из task entity в task dto")
    void testConvertTaskToDto() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test_title");
        task.setDescription("test_description");

        TaskDto taskDto = taskMapper.convertTaskToDto(task);

        Assertions.assertNotNull(taskDto);
        Assertions.assertEquals(task.getId(), taskDto.getId());
        Assertions.assertEquals(task.getTitle(), taskDto.getTitle());;
    }

    @Test
    @DisplayName("Тест маппинга из листа task entity в лист task dto")
    void testConvertTaskListToDto() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("First task");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Second task");

        List<Task> taskList = List.of(task1, task2);
        List<TaskDto> taskDtoList = taskMapper.convertTaskListToDto(taskList);

        Assertions.assertNotNull(taskDtoList);
        Assertions.assertEquals(2, taskDtoList.size());
        Assertions.assertEquals(taskList.get(0).getId(), taskDtoList.get(0).getId());
        Assertions.assertEquals(taskList.get(1).getId(), taskDtoList.get(1).getId());
    }
}