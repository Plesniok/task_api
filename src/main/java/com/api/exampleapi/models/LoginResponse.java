package com.api.exampleapi.models;

public class LoginResponse {
    public String newToken;

    public LoginResponse(String newToken){
        this.newToken = newToken;
    }

    public String getNewToken() {
        return newToken;
    }
}
