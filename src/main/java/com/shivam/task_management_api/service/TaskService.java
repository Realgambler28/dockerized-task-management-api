package com.shivam.task_management_api.service;

import com.shivam.task_management_api.dto.TaskRequest;
import com.shivam.task_management_api.dto.TaskResponse;
import com.shivam.task_management_api.entity.Task;
import com.shivam.task_management_api.exception.TaskNotFoundException;
import com.shivam.task_management_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // GET all tasks
    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET task by ID
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task with ID " + id + " not found"
                        ));

        return convertToResponse(task);
    }

    // CREATE task
    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(task);

        return convertToResponse(savedTask);
    }

    // UPDATE task
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task with ID " + id + " not found"
                        ));

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setCompleted(request.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);

        return convertToResponse(updatedTask);
    }

    // DELETE task
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task with ID " + id + " not found"
                        ));

        taskRepository.delete(task);
    }

    // Convert Entity to Response DTO
    private TaskResponse convertToResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}