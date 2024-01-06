package com.api.exampleapi.controllers;

import com.api.exampleapi.models.LoginResponse;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.database.enities.UserEnity;

import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.constraints.Email;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public LoginResponse getHelloWorld(@Email @Valid String email, String password) throws BadRequestException {
        System.out.println("get user");
        List<UserEnity> ifUserExist = userRepository.findByEmailAndPassword(email, password);

        if(ifUserExist.size() == 0) {
            throw new BadRequestException("invalid login data");
        }

        UserEnity userData = ifUserExist.get(0);

        String newUserToken = JwtService.generateNewToken(
                email,
                userData.getRole(),
                0
        );

        userData.setToken(newUserToken);

        userRepository.save(userData);

        return new LoginResponse(200, newUserToken);

    }

    @PostMapping("/user")
    public ManagementResponseModel addUser(
            @Valid @RequestBody UserEnity requestData,
            @RequestHeader("X-access-token") String userToken
    ) throws BadRequestException {

        Claims decodedUserTokenPayload = JwtService.decodeTokenToPayload(userToken);

        if (decodedUserTokenPayload == null) {

            throw new SignatureException("Invalid token");
        }

        if (!decodedUserTokenPayload.get("role").equals("admin")) {

            throw new BadRequestException("You dont have permission for this action");
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

        throw new BadRequestException("Internal server error");
    }

    @PostMapping("/user/self")
    public ManagementResponseModel addUserBySelf(
            @Valid @RequestBody UserEnity requestData
    ) throws BadRequestException {

        String newUserToken = JwtService.generateNewToken(
                requestData.getEmail(),
                requestData.getRole(),
                0
        );

        requestData.setToken(newUserToken);

        if( userRepository.save(requestData).equals(requestData)){

            return new ManagementResponseModel(200, "ok");
        };

        throw new BadRequestException("Internal server error");
    }

}
