package com.api.exampleapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarkTaskAsDoneRequest {
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
