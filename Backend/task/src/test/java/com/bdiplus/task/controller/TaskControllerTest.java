package com.bdiplus.task.controller;

import com.bdiplus.task.model.Task;
import com.bdiplus.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;


    Task taskOne;
    Task taskTwo;

    List<Task> taskList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        taskOne = new Task(1L,"Do Exercise", "Daily do exercise from 6am to 7am.");
        taskTwo = new Task(2L,"Do Walking", "Daily do exercise from 7am to 8am.");

        taskList.add(taskOne);
        taskList.add(taskTwo);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAllTasks() throws Exception{
        when(taskService.findAll())
                .thenReturn(taskList);

        this.mockMvc.perform(get("/tasks"))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testGetTask() throws Exception{
        when(taskService.findById(1L))
                .thenReturn(Optional.ofNullable(taskOne));

        this.mockMvc.perform(get("/tasks/1"))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    void testCreateTask() throws Exception {
        // Create a Task object to use in the request body
        Task newTask = new Task(3L, "New Task", "Description of the new task");

        // Mock the behavior of your taskService to save the task
        when(taskService.save(any(Task.class)))
                .thenReturn(newTask);

        // Convert the Task object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(newTask);

        // Perform the POST request with the JSON data
        this.mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    void testCreateTaskConflict() throws Exception {
        // Create a Task object to use in the request body
        Task newTask = new Task(3L, "New Task", "Description of the new task");

        // Mock the behavior of your taskService to return null (conflict)
        when(taskService.save(any(Task.class)))
                .thenReturn(null);

        // Convert the Task object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(newTask);

        // Perform the POST request with the JSON data
        this.mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andDo(print())
                .andExpect(status().isConflict());
    }

//    @Test
//    void testCreateTaskInternalServerError() throws Exception {
//        // Create a Task object to use in the request body
//        Task newTask = new Task(3L, "New Task", "Description of the new task");
//
//        // Mock the behavior of your taskService to throw an exception
//        when(taskService.save(any(Task.class)))
//                .thenThrow(new Exception("Internal Server Error"));
//
//        // Convert the Task object to JSON format
//        ObjectMapper objectMapper = new ObjectMapper();
//        String taskJson = objectMapper.writeValueAsString(newTask);
//
//        // Perform the POST request with the JSON data and expect an internal server error
//        this.mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(taskJson))
//                .andDo(print())
//                .andExpect(status().isInternalServerError());
//    }

//    @Test
//    void testCreateTaskInternalServerError() throws Exception {
//        // Create a Task object to use in the request body
//        Task newTask = new Task(3L, "New Task", "Description of the new task");
//
//        // Mock the behavior of your taskService to throw an exception
//        when(taskService.save(any(Task.class)))
//                .thenThrow(new Exception("Internal Server Error"));
//
//        // Convert the Task object to JSON format
//        ObjectMapper objectMapper = new ObjectMapper();
//        String taskJson = objectMapper.writeValueAsString(newTask);
//
//        // Perform the POST request with the JSON data
//        this.mockMvc.perform(post("/tasks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(taskJson))
//                .andDo(print())
//                .andExpect(status().isInternalServerError());
//    }

    @Test
    void testUpdateTask() throws Exception {
        // Create a Task object to use in the request body
        Task updatedTask = new Task(2L, "Updated Task", "Description of the updated task");

        // Mock the behavior of your taskService to find the task by ID
        when(taskService.findById(2L))
                .thenReturn(Optional.of(taskTwo));

        // Mock the behavior of your taskService to save the updated task
        when(taskService.save(any(Task.class)))
                .thenReturn(updatedTask);

        // Convert the updated Task object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(updatedTask);

        // Perform the PUT request with the JSON data and the ID
        this.mockMvc.perform(put("/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void testDeleteTask() throws Exception {
        // Mock the behavior of your taskService
        when(taskService.findById(2L))
                .thenReturn(Optional.of(taskTwo));

        // Perform the DELETE request
        this.mockMvc.perform(delete("/tasks/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    void testDeleteTask() throws Exception {
//        // Mock the findById() and deleteById() methods in the task service
//        Mockito.when(taskService.findById(2L)).thenReturn(Optional.of(new Task()));
//        Mockito.when(taskService.deleteById(2L)).thenReturn(
//
//        );
//
//        // Perform a DELETE request to the /tasks/{id} endpoint
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/tasks/{id}", 2L)
//                        .contentType(MediaType.APPLICATION_JSON))
//
//                // Expect a 200 OK response
//                .andExpect(status().isOk());
//
//        // Verify that the findById() and deleteById() methods were called on the task service
//        Mockito.verify(taskService).findById(2L);
//        Mockito.verify(taskService).deleteById(2L);
//    }

//    @Test
//    void testDeleteTask() throws Exception {
//        // Mock the deleteById method in the task service
//        Mockito.when(taskService.deleteById(2L)).thenReturn(null);
//
//        // Perform a DELETE request to the /tasks/{id} endpoint
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/tasks/{id}", 2L)
//                        .contentType(MediaType.APPLICATION_JSON))
//
//                // Expect a 200 OK response
//                .andExpect(status().isOk());
//
//        // Verify that the deleteById method was called on the task service
//        Mockito.verify(taskService).deleteById(2L);
//    }


//    @Test
//    void deleteTask() {
//
//        when(taskService.deleteById(2L))
//                .thenReturn("")
//    }
}