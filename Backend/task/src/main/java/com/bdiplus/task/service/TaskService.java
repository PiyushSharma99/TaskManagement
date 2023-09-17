package com.bdiplus.task.service;

import com.bdiplus.task.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {


    Task save(Task task);
    void deleteById(Long taskId);
    Optional<Task> findById(Long taskId);
    List<Task> findAll();


}
