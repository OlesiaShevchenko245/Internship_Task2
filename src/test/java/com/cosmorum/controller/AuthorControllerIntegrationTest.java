package com.cosmorum.controller;

import com.cosmorum.dto.AuthorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class AuthorControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAuthor() throws Exception {
        AuthorDTO author = new AuthorDTO(null, "Carl", "Sagan", "American");

        mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Carl"))
                .andExpect(jsonPath("$.lastName").value("Sagan"))
                .andExpect(jsonPath("$.nationality").value("American"));
    }

    @Test
    void testCreateAuthorWithDuplicateNationality() throws Exception {
        AuthorDTO author1 = new AuthorDTO(null, "First", "Author", "TestNation");
        AuthorDTO author2 = new AuthorDTO(null, "Second", "Author", "TestNation");

        mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author2)))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateAuthorWithInvalidData() throws Exception {
        AuthorDTO author = new AuthorDTO(null, "", "", "");

        mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllAuthors() throws Exception {
        mockMvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testUpdateAuthor() throws Exception {
        AuthorDTO author = new AuthorDTO(null, "Neil", "Armstrong", "USNation");

        String createResponse = mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        AuthorDTO created = objectMapper.readValue(createResponse, AuthorDTO.class);
        created.setFirstName("Neil Alden");

        mockMvc.perform(put("/api/author/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Neil Alden"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        AuthorDTO author = new AuthorDTO(null, "Test", "Delete", "DeleteNation");

        String createResponse = mockMvc.perform(post("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        AuthorDTO created = objectMapper.readValue(createResponse, AuthorDTO.class);

        mockMvc.perform(delete("/api/author/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/author/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
