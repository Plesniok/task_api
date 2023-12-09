package com.api.exampleapi.database.repositories;

import com.api.exampleapi.database.enities.TaskEnity;
import com.api.exampleapi.database.enities.UserEnity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEnity, Long> {

    List<TaskEnity> findByUserId(int userId);

    TaskEnity findByIdAndUserId(int id, int userId);

}
