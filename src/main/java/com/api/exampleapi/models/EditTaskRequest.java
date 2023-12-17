package com.api.exampleapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class EditTaskRequest {
    @NotNull(message = "Task id cannot be null")
    private int taskId;

    @NotNull(message = "description cannot be null")
    private String description;

    @NotNull(message = "deadline cannot be null")
    private int deadlineTimestamp;

    public EditTaskRequest(
            @JsonProperty("taskId") int taskId,
            @JsonProperty("description") String description,
            @JsonProperty("deadlineTimestamp") int deadlineTimestamp
    ){
        this.taskId = taskId;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setDeadlineTimestamp(int deadlineTimestamp) {
        this.deadlineTimestamp = deadlineTimestamp;
    }
    public int getDeadlineTimestamp() {
        return deadlineTimestamp;
    }
}
