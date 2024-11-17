package com.example.trainingt1.service;


import com.example.trainingt1.aspect.LogException;
import com.example.trainingt1.aspect.LogExecutionTime;
import com.example.trainingt1.aspect.LogHandling;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.exceptions.NotFoundByIdException;
import com.example.trainingt1.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private boolean existTaskById(Long id) {
        return taskRepository.existsById(id);
    }

    private void checkTaskIdExists(Long id) throws NotFoundByIdException {
        if (!existTaskById(id)) {
            throw new NotFoundByIdException(Task.class.getName(), id);
        }
    }

    @LogExecutionTime
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @LogHandling
    @LogException
    public Task getTaskById(Long id) throws NotFoundByIdException {
        checkTaskIdExists(id);
        return taskRepository.getReferenceById(id);
    }

    @LogHandling
    @LogException
    public Task updateTaskById(Long id, Task task) throws NotFoundByIdException {
        checkTaskIdExists(id);
        task.setId(id);
        return taskRepository.save(task);
    }

    @LogExecutionTime
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @LogException
    public void deleteTaskById(Long id) throws NotFoundByIdException {
        checkTaskIdExists(id);
        taskRepository.deleteById(id);
    }


}
