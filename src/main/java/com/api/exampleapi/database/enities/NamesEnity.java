package com.api.exampleapi.database.enities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "names")
public class NamesEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    public NamesEnity() {}

    public NamesEnity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
