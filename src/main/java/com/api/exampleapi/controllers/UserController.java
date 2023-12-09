package com.api.exampleapi.controllers;

import com.api.exampleapi.models.LoginResponse;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.database.enities.UserEnity;
import com.api.exampleapi.database.repositories.NamesRepository;
import com.api.exampleapi.database.enities.NamesEnity;
import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.List;

@Validated
@RestController
public class UserController {
    private final NamesRepository namesRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(NamesRepository namesRepository, UserRepository userRepository) {
        this.namesRepository = namesRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    public String getHelloWorld(String test) {
        return test;
    }

    @PostMapping("/create")
    public NamesEnity createEntity(@Valid @RequestBody NamesEnity requestData) {

        return namesRepository.save(requestData);
    }

    @GetMapping("/login")
    public LoginResponse getHelloWorld(String email, String password) {
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
            return new ManagementResponseModel(400, "problem with decoding user token");
        }

        if (!decodedUserTokenPayload.get("role").equals("admin")) {
            return new ManagementResponseModel(400, "You dont have permission for this action");
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

        return new ManagementResponseModel(400, "not ok");
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

        return new ManagementResponseModel(400, "not ok");
    }

}
