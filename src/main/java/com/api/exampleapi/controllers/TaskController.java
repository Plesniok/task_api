package com.api.exampleapi.controllers;

import com.api.exampleapi.database.enities.TaskEnity;
import com.api.exampleapi.database.repositories.TaskRepository;
import com.api.exampleapi.models.EditTaskRequest;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.models.MarkTaskAsDoneRequest;
import com.api.exampleapi.models.TaskListsResponse;
import com.api.exampleapi.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
public class TaskController {
    private final TaskRepository taskRepository;
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/task")
    public ManagementResponseModel addTask(
            @Valid @RequestBody TaskEnity requestData,
            @RequestHeader("X-access-token") String userToken
    ) throws BadRequestException {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {

            throw new SignatureException("Invalid token");
        }

        requestData.setUserId(Integer.parseInt(decodedUserTokenPayload.get("id").toString()));
        requestData.setIsDone(false);

        if( taskRepository.save(requestData).equals(requestData)){

            return new ManagementResponseModel(200, "ok");
        };

        throw new BadRequestException("Internal server error");
    }

    @PatchMapping("/task/done")
    public ManagementResponseModel markTaskAsDone(
            @RequestBody MarkTaskAsDoneRequest requestData,
            @RequestHeader("X-access-token") String userToken
    ) throws BadRequestException {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            throw new SignatureException("Invalid token");
        }

        TaskEnity foundTask = taskRepository.findByIdAndUserId(requestData.getTaskId(), Integer.parseInt(decodedUserTokenPayload.get("id").toString()));

        if( foundTask == null){
            throw new BadRequestException("Task does not exist");
        };

        foundTask.setIsDone(true);

        if( taskRepository.save(foundTask).equals(foundTask)){

            return new ManagementResponseModel(200, "ok");
        };

        throw new BadRequestException("Internal server error");
    }

    @PatchMapping("/task")
    public ManagementResponseModel editTask(
            @RequestBody EditTaskRequest requestData,
            @RequestHeader("X-access-token") String userToken
    ) throws BadRequestException {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            throw new SignatureException("Invalid token");
        }

        TaskEnity foundTask = taskRepository.findByIdAndUserId(requestData.getTaskId(), Integer.parseInt(decodedUserTokenPayload.get("id").toString()));

        if( foundTask == null){
            throw new BadRequestException("task does not exist");
        };

        foundTask.setDescription(requestData.getDescription());
        foundTask.setDeadlineTimestamp(requestData.getDeadlineTimestamp());

        if( taskRepository.save(foundTask).equals(foundTask)){

            return new ManagementResponseModel(200, "ok");
        };

        throw new BadRequestException("Internal server error");
    }

    @GetMapping("/tasks")
    public TaskListsResponse getUserTasks(
            @RequestHeader("X-access-token") String userToken
    ) {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            throw new SignatureException("Invalid token");

        }

        return new TaskListsResponse(taskRepository.findByUserId(Integer.parseInt(decodedUserTokenPayload.get("id").toString())));
    }

}
