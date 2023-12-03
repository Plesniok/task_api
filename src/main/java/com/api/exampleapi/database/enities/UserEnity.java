package com.api.exampleapi.database.enities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class UserEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Email cannot be null")
    private String role;

    private String token;
    @NotNull(message = "Password cannot be null")
    private String password;

    public UserEnity() {}

    public UserEnity(String email, String password, String token, String role) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
