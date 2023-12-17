package com.api.exampleapi.controllers;

import com.api.exampleapi.models.LoginResponse;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.database.enities.UserEnity;
import com.api.exampleapi.database.repositories.NamesRepository;
import com.api.exampleapi.database.enities.NamesEnity;
import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @GetMapping("/login")
    public LoginResponse getHelloWorld(@Email @Valid String email, String password) {
        List<UserEnity> ifUserExist = userRepository.findByEmailAndPassword(email, password);

        if(ifUserExist.size() == 0) {
            return new LoginResponse("Invalid login data");
        }

        UserEnity userData = ifUserExist.get(0);

        return new LoginResponse(userData.getToken());

    }

    @PostMapping("/user")
    public ManagementResponseModel addUser(
            @Valid @RequestBody UserEnity requestData,
            @RequestHeader("X-access-token") String userToken
    ) {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {
            return new ManagementResponseModel(401, "problem with decoding user token");
        }

        if (!decodedUserTokenPayload.get("role").equals("admin")) {
            return new ManagementResponseModel(401, "You dont have permission for this action");
        }


        String newUserToken = JwtService.generateNewToken(
                requestData.getEmail(),
                requestData.getRole(),
                0
        );

        requestData.setToken(newUserToken);

        if( userRepository.save(requestData).equals(requestData)){

            return new ManagementResponseModel(200, "ok");
        };

        return new ManagementResponseModel(500, "Internal server error");
    }

    @PostMapping("/user/self")
    public ManagementResponseModel addUserBySelf(
            @Valid @RequestBody UserEnity requestData
    ) {

        String newUserToken = JwtService.generateNewToken(
                requestData.getEmail(),
                requestData.getRole(),
                0
        );

        requestData.setToken(newUserToken);

        if( userRepository.save(requestData).equals(requestData)){

            return new ManagementResponseModel(200, "ok");
        };

        return new ManagementResponseModel(500, "Internal server error");
    }

}
