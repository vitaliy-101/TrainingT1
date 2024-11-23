package com.example.trainingt1.controller;


import com.example.trainingt1.aspect.LogHandlingDto;
import com.example.trainingt1.dto.TaskDto;
import com.example.trainingt1.dto.TaskDtoIn;
import com.example.trainingt1.exceptions.NotFoundByIdException;
import com.example.trainingt1.mapper.TaskMapper;
import com.example.trainingt1.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @LogHandlingDto
    @PostMapping
    public TaskDto createTask(@RequestBody TaskDtoIn taskDtoIn) {
        return taskMapper.convertTaskToDto(taskService.createTask(taskMapper.convertToTaskEntity(taskDtoIn)));
    }

    @GetMapping
    public List<TaskDto> getAllTask() {
        return taskMapper.convertTaskListToDto(taskService.getAllTask());
    }

    @LogHandlingDto
    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) throws NotFoundByIdException {
        return taskMapper.convertTaskToDto(taskService.getTaskById(id));
    }

    @LogHandlingDto
    @PutMapping("/{id}")
    public TaskDto updateTaskById(@PathVariable Long id,
                                  @RequestBody TaskDtoIn taskDtoIn) throws NotFoundByIdException {
        return taskMapper.convertTaskToDto(taskService.updateTaskById(id, taskMapper.convertToTaskEntity(taskDtoIn)));
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id) throws NotFoundByIdException {
        taskService.deleteTaskById(id);
    }

}
