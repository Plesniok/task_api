package com.api.exampleapi.models;

import com.api.exampleapi.database.enities.TaskEnity;
import java.util.List;
public class TaskListsResponse {
    public List<TaskEnity> tasks;

    public TaskListsResponse(List<TaskEnity> tasks){
        this.tasks = tasks;
    }

    public List<TaskEnity> getTasks() {
        return tasks;
    }
}
