package com.api.exampleapi.models;

public class LoginResponse {
    public String newToken;
    public int code;

    public LoginResponse(int code, String newToken){
        this.newToken = newToken;
        this.code = code;
    }

    public String getNewToken() {
        return newToken;
    }
}
