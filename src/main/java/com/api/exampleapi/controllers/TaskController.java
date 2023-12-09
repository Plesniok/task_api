package com.api.exampleapi.controllers;

import com.api.exampleapi.database.enities.TaskEnity;
import com.api.exampleapi.database.repositories.TaskRepository;
import com.api.exampleapi.models.LoginResponse;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.database.enities.UserEnity;
import com.api.exampleapi.database.repositories.NamesRepository;
import com.api.exampleapi.database.enities.NamesEnity;
import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.models.MarkTaskAsDoneRequest;
import com.api.exampleapi.models.TaskListsResponse;
import com.api.exampleapi.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.List;

@Validated
@RestController
public class TaskController {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping("/task")
    public ManagementResponseModel addTask(
            @Valid @RequestBody TaskEnity requestData,
            @RequestHeader("X-access-token") String userToken
    ) {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            return new ManagementResponseModel(400, "problem with decoding user token");
        }

        requestData.setUserId(Integer.parseInt(decodedUserTokenPayload.get("id").toString()));
        requestData.setIsDone(false);

        if( taskRepository.save(requestData).equals(requestData)){

            return new ManagementResponseModel(200, "ok");
        };

        return new ManagementResponseModel(400, "not ok");
    }

    @PatchMapping("/task")
    public ManagementResponseModel patchTaskAsDone(
            @RequestBody MarkTaskAsDoneRequest requestData,
            @RequestHeader("X-access-token") String userToken
    ) {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            return new ManagementResponseModel(400, "problem with decoding user token");
        }

        TaskEnity foundTask = taskRepository.findByIdAndUserId(requestData.getTaskId(), Integer.parseInt(decodedUserTokenPayload.get("id").toString()));

        if( foundTask == null){
            return new ManagementResponseModel(400, "Task doesnt exist");
        };

        foundTask.setIsDone(true);

        if( taskRepository.save(foundTask).equals(foundTask)){

            return new ManagementResponseModel(200, "ok");
        };

        return new ManagementResponseModel(500, "something gone wrong");
    }

    @GetMapping("/task")
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
