package com.api.exampleapi.database.enities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tasks")
public class TaskEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String title;

    private int userId;

    @NotNull(message = "description cannot be null")
    private String description;

    @NotNull(message = "deadline cannot be null")
    private int deadlineTimestamp;

    private boolean isDone;

    public TaskEnity() {}

    public TaskEnity(
            String title,
            int userId,
            String description,
            int deadlineTimestamp
    ) {
        this.title = title;
        this.userId = userId;
        this.description = description;
        this.deadlineTimestamp = deadlineTimestamp;
    }

    public String getTitle() {
        return title;
    }

    public int getDeadlineTimestamp() {
        return deadlineTimestamp;
    }

    public int getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setDeadlineTimestamp(int deadlineTimestamp) {
        this.deadlineTimestamp = deadlineTimestamp;
    }

}
