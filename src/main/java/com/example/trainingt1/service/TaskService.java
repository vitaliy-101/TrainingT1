package com.example.trainingt1.service;


import com.example.trainingt1.aspect.LogException;
import com.example.trainingt1.aspect.LogExecutionTime;
import com.example.trainingt1.aspect.LogHandling;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.TaskStatus;
import com.example.trainingt1.exceptions.NotFoundByIdException;
import com.example.trainingt1.kafka.KafkaTaskStatusProducer;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import com.example.trainingt1.notification.NotificationService;
import com.example.trainingt1.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final KafkaTaskStatusProducer kafkaTaskStatusProducer;
    private final NotificationService notificationService;
    @Value("${spring.kafka.topic.updating_tasks}")
    private String updatingTaskTopic;

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
        task.setStatus(TaskStatus.OPENED);
        return taskRepository.save(task);
    }

    @LogHandling
    @LogException
    @Transactional
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

    @Transactional
    public Task updateTaskStatus(TaskStatusDto taskStatusDto) throws NotFoundByIdException {
        Task task = getTaskById(taskStatusDto.getId());
        task.setStatus(taskStatusDto.getStatus());
        return taskRepository.save(task);
    }

    public void sendNewTaskStatus(TaskStatusDto taskStatusDto) {
        kafkaTaskStatusProducer.send(updatingTaskTopic, taskStatusDto);
    }


}
