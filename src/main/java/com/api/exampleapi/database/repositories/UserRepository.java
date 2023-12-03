package com.api.exampleapi.database.repositories;

import com.api.exampleapi.database.enities.UserEnity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEnity, Long> {

    List<UserEnity> findByEmailAndPassword(String email, String password);

}
