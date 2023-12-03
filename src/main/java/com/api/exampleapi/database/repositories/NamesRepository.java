package com.api.exampleapi.database.repositories;

import com.api.exampleapi.database.enities.NamesEnity;
import org.springframework.data.repository.CrudRepository;

public interface NamesRepository extends CrudRepository<NamesEnity, Long> {

}
