package com.api.exampleapi.models;

public class ResponseModel {
    public int code;
    public String errorMessage;

    public ResponseModel(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
