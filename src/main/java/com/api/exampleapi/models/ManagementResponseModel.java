package com.api.exampleapi.models;

public class ManagementResponseModel {
    public int code;
    public String errorMessage;

    public ManagementResponseModel(int code, String errorMessage) {
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
