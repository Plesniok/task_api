package com.api.exampleapi;

import com.api.exampleapi.database.enities.TaskEnity;
import com.api.exampleapi.database.enities.UserEnity;
import com.api.exampleapi.database.repositories.UserRepository;
import com.api.exampleapi.models.EditTaskRequest;
import com.api.exampleapi.models.ManagementResponseModel;
import com.api.exampleapi.models.MarkTaskAsDoneRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

//@AutoConfigureTestDatabase()
class ExampleApiApplicationTests {

    private String userEmail = "damiedasan@sfas.pl";

    private String userPassword = "132Qaz321!";

    private String userToken;

    private int taskId = 1;



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Test
    @Order(1)
    void addUser() throws Exception {
        UserEnity newUser = new UserEnity(
                userEmail,
                userPassword,
                null,
                "admin"
        );

        ResultActions response = mockMvc.perform(post("/user/self")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        response.andDo(print())
                .andExpect(jsonPath("$.code",
                        is(200)));

    }

    @Test
    @Order(2)

    void successFlow() throws Exception {

        ResultActions response = mockMvc.perform(get("/login")
                .param("email", userEmail)
                .param("password", userPassword)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk());

        String responseBody = response.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String newUserToken = jsonNode.get("newToken").asText();

        addTask(newUserToken);

        int newTaskId = getTaskId(newUserToken);

        editTask(newUserToken, newTaskId);

        markTaskAsDone(newUserToken, newTaskId);

    }

    void addTask(String token) throws Exception {
        TaskEnity newTask = new TaskEnity();

        newTask.setDescription("test description");
        newTask.setTitle("testTitle");
        newTask.setDeadlineTimestamp(1734797599);

        ResultActions response = mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask))
                .header("X-access-token", token));

        response.andDo(print())
                .andExpect(jsonPath("$.code",
                        is(200)));

    }

    void editTask(String token, int taskId) throws Exception {
        EditTaskRequest newTaskData = new EditTaskRequest(
                taskId,
                "test description EDITED",
                1734797599
        );


        ResultActions response = mockMvc.perform(patch("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTaskData))
                .header("X-access-token", token));

        response.andDo(print())
                .andExpect(jsonPath("$.code",
                        is(200)));

    }

    void markTaskAsDone(String token, int taskId) throws Exception {
        MarkTaskAsDoneRequest newTaskData = new MarkTaskAsDoneRequest(
                taskId
        );


        ResultActions response = mockMvc.perform(patch("/task/done")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTaskData))
                .header("X-access-token", token));

        response.andDo(print())
                .andExpect(jsonPath("$.code",
                        is(200)));

    }

    int getTaskId(String token) throws Exception {

        ResultActions response = mockMvc.perform(get("/tasks")
                .header("X-access-token", token)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk());

        String responseBody = response.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if (jsonNode.has("tasks") && jsonNode.get("tasks").isArray()) {
            JsonNode firstTaskNode = jsonNode.get("tasks").get(0);

            return firstTaskNode.get("id").asInt();
        }

        return 0;

    }
}
