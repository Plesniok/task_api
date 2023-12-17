package com.api.exampleapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class MarkTaskAsDoneRequest {
    @NotNull(message = "Task id cannot be null")
    private int taskId;

    public MarkTaskAsDoneRequest(@JsonProperty("taskId") int taskId){
        this.taskId = taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public int getTaskId() {
        return taskId;
    }
}
