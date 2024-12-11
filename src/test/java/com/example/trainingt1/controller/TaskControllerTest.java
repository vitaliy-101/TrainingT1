package com.example.trainingt1.controller;

import com.example.trainingt1.dto.LoginRequest;
import com.example.trainingt1.dto.LoginResponse;
import com.example.trainingt1.dto.SignupRequest;
import com.example.trainingt1.dto.TaskDtoRequest;
import com.example.trainingt1.entity.Task;
import com.example.trainingt1.entity.TaskStatus;
import com.example.trainingt1.entity.role.RoleEnum;
import com.example.trainingt1.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        SignupRequest signupRequest = new SignupRequest("user", "password", List.of(RoleEnum.ROLE_USER));
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest("user", "password");
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(response, LoginResponse.class);
        jwtToken = loginResponse.getJwt();

        taskRepository.deleteAll();
        saveTestTasks();
    }

    @Test
    @DisplayName("Тест на получение всех тасок")
    void testGetTasks() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Тест на создание таски")
    void testCreateTask() throws Exception {
        TaskDtoRequest taskDto = new TaskDtoRequest();
        taskDto.setTitle("test_title");
        taskDto.setDescription("test_description");
        taskDto.setStatus(TaskStatus.OPENED);
        taskDto.setUserId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("test_title"));
    }

    @Test
    @DisplayName("Тест на получение таски по id")
    void testGetTaskById() throws Exception {
        mockMvc.perform(get("/tasks/2")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test_title2"));
    }

    @Test
    @DisplayName("Ошибочный тест на получение таски по id")
    void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/tasks/3")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
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
