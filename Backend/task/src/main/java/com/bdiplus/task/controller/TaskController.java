package com.bdiplus.task.controller;

import com.bdiplus.task.constants.Constant;
import com.bdiplus.task.model.Task;
import com.bdiplus.task.response.ResponseHandler;
import com.bdiplus.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")

public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<Object> getAllTasks() {
        try {
            List<Task> tasks = taskService.findAll();

            if (tasks.isEmpty() || tasks.size() == 0) {
                return ResponseHandler.generateResponse(Constant.NO_DATA_MESSAGE, HttpStatus.NO_CONTENT, null);
            }
            return ResponseHandler.generateResponse(Constant.SUCCESS_GET_MESSAGE, HttpStatus.OK, tasks);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<Object> getTask(@PathVariable Long id) {
        try {
            Optional<Task> searchedTask = taskService.findById(id);
            if (searchedTask.isPresent()) {
                return ResponseHandler.generateResponse(Constant.SUCCESS_GET_MESSAGE, HttpStatus.OK, searchedTask.get());

            }


            return ResponseHandler.generateResponse(Constant.NO_DATA_MESSAGE, HttpStatus.CONFLICT, null);


        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @PostMapping("/tasks")
    public ResponseEntity<Object> createTask(@RequestBody Task task)
    {

        System.out.println(task);
        try {
            System.out.println(task);

            Task savedTask = taskService.save(task);
            System.out.println(savedTask);
            if (savedTask != null) {
                return ResponseHandler.generateResponse(Constant.SUCCESS_ADD_MESSAGE, HttpStatus.CREATED, savedTask);
            }
            return ResponseHandler.generateResponse(Constant.CONFLICT_MESSAGE_TASK_EXIST, HttpStatus.CONFLICT, null);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(Constant.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Object> updateTask(@RequestBody Task task, @PathVariable Long id) {
        try {
            Optional<Task> existTask = taskService.findById(id);
            if (existTask.isPresent()) {
                Task searchedTask = existTask.get();
                searchedTask.setTitle(task.getTitle());
                searchedTask.setDescription(task.getDescription());


                Task updatedTask = taskService.save(searchedTask);
                return ResponseHandler.generateResponse(Constant.UPDATE_SUCCESS_MESSAGE, HttpStatus.OK, updatedTask);
            }
            return ResponseHandler.generateResponse(Constant.CONFLICT_MESSAGE_TASK_NOT_PRESENT, HttpStatus.CONFLICT, null);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(Constant.CONFLICT_MESSAGE, HttpStatus.CONFLICT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long id) {
        try {
            Optional<Task> deletedTask = taskService.findById(id);

            if (deletedTask.isPresent()) {
                taskService.deleteById(id);
                return ResponseHandler.generateResponse(Constant.DELETE_SUCCESS_MESSAGE, HttpStatus.OK, deletedTask);
            }
            return ResponseHandler.generateResponse(Constant.NO_DATA_MESSAGE, HttpStatus.CONFLICT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
