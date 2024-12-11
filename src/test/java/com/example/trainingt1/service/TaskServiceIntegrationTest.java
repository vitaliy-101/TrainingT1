package com.example.trainingt1.service;

import com.example.trainingt1.AbstractContainerBaseTest;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.TaskStatus;
import com.example.trainingt1.exceptions.NotFoundByIdException;
import com.example.trainingt1.kafka.dto.TaskStatusDto;
import com.example.trainingt1.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class TaskServiceIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        saveTestTasks();
    }

    @Test
    @DisplayName("Тестирование изменения статуса у таски и отправки по kafka")
    public void sendNewTaskStatusTest() throws NotFoundByIdException, InterruptedException {
        TaskStatusDto taskStatusDto = new TaskStatusDto(1L, TaskStatus.CLOSED);
        taskService.sendNewTaskStatus(taskStatusDto);
        Thread.sleep(5000);
        Task task = taskService.getTaskById(1L);
        Assertions.assertEquals(TaskStatus.CLOSED, task.getStatus());
    }

    @Test
    @DisplayName("Проверка удаления таски из репозитория")
    public void deleteByIdTest() throws NotFoundByIdException {
        taskService.deleteTaskById(1L);
        List<Task> tasks = taskService.getAllTask();
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Ошибочная проверка удаления таски из репозитория")
    public void deleteByIdTestError() {
        Assertions.assertThrows(NotFoundByIdException.class, () -> taskService.deleteTaskById(3L));
    }

    @Test
    @DisplayName("Проверка получения таски по id")
    public void getTaskById() throws NotFoundByIdException {
        Task task = taskService.getTaskById(2L);
        Assertions.assertNotNull(task);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        Assertions.assertEquals("test_title2", task.getTitle());
        Assertions.assertEquals("test_description2", task.getDescription());
    }


    private void saveTestTasks() {
        Task task1 = new Task();
        task1.setTitle("test_title1");
        task1.setDescription("test_description1");
        task1.setStatus(TaskStatus.OPENED);
        task1.setUserId(1L);

        Task task2 = new Task();
        task2.setTitle("test_title2");
        task2.setDescription("test_description2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setUserId(10L);

        taskRepository.save(task1);
        taskRepository.save(task2);
    }


}
