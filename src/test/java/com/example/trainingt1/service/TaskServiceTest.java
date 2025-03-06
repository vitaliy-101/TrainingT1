package com.example.trainingt1.service;

import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.TaskStatus;
import com.example.trainingt1.exceptions.NotFoundByIdException;
import com.example.trainingt1.kafka.KafkaTaskStatusProducer;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import com.example.trainingt1.notification.NotificationService;
import com.example.trainingt1.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private KafkaTaskStatusProducer kafkaTaskStatusProducer;
    @Mock
    private NotificationService notificationService;

    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("test_title");
        task.setDescription("test_description");
        task.setStatus(TaskStatus.OPENED);
        task.setUserId(1L);
        taskService = new TaskService(taskRepository, kafkaTaskStatusProducer, notificationService);
    }

    @Test
    @DisplayName("Тест создания задачи")
    void createTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task createdTask = taskService.createTask(task);
        Assertions.assertNotNull(createdTask);
        Assertions.assertEquals("test_title", createdTask.getTitle());
        Assertions.assertEquals("test_description", createdTask.getDescription());
        Assertions.assertEquals(TaskStatus.OPENED, createdTask.getStatus());
    }

    @Test
    @DisplayName("Тест получения задачи по ID")
    void getTaskById() throws NotFoundByIdException {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.getReferenceById(1L)).thenReturn(task);
        Task foundTask = taskService.getTaskById(1L);
        Assertions.assertNotNull(foundTask);
        Assertions.assertEquals(1L, foundTask.getId());
        Assertions.assertEquals("test_title", foundTask.getTitle());
    }

    @Test
    @DisplayName("Тест обновления задачи по ID")
    void updateTaskById() throws NotFoundByIdException {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updateTask = new Task();
        updateTask.setTitle("update_title");
        updateTask.setDescription("update_description");
        updateTask.setStatus(TaskStatus.IN_PROGRESS);
        Task task = taskService.updateTaskById(1L, updateTask);

        Assertions.assertNotNull(task);
        Assertions.assertEquals("test_title", task.getTitle());
        Assertions.assertEquals("test_description", task.getDescription());
        Assertions.assertEquals(TaskStatus.OPENED, task.getStatus());
    }

    @Test
    @DisplayName("Тест получения всех задач")
    void getAllTasks() {
        List<Task> tasks = Collections.singletonList(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        List<Task> allTasks = taskService.getAllTask();
        Assertions.assertNotNull(allTasks);
        Assertions.assertEquals(1, allTasks.size());
    }

    @Test
    @DisplayName("Тест удаления задачи по ID")
    void deleteTaskById() throws NotFoundByIdException {
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.deleteTaskById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Тест обновления статуса задачи")
    void updateTaskStatus() throws NotFoundByIdException {
        TaskStatusDto taskStatusDto = new TaskStatusDto(1L, TaskStatus.CLOSED);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.getReferenceById(1L)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task updatedTask = taskService.updateTaskStatus(taskStatusDto);
        Assertions.assertNotNull(updatedTask);
        Assertions.assertEquals(TaskStatus.CLOSED, updatedTask.getStatus());
    }

    @Test
    @DisplayName("Тест ошибки: Задача не найдена по ID")
    void testTaskNotFoundException() {
        when(taskRepository.existsById(1L)).thenReturn(false);
        NotFoundByIdException exception = Assertions.assertThrows(NotFoundByIdException.class, () -> taskService.getTaskById(1L));
        Assertions.assertEquals("Entity named com.example.trainingt1.entity.Task is not found by id = 1", exception.getMessage());
    }
}
