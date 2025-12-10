package com.cosmorum.controller;

import com.cosmorum.dto.ObservationDTO;
import com.cosmorum.dto.ObservationFilterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
class ObservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateObservation() throws Exception {
        ObservationDTO observation = new ObservationDTO();
        observation.setName("Test Observation");
        observation.setDescription("Test description");
        observation.setObservationTime(LocalDateTime.of(2024, 1, 15, 20, 30));
        observation.setAuthorId(1L);
        observation.setCelestialObjects(Arrays.asList("Mars", "Jupiter"));

        mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Observation"))
                .andExpect(jsonPath("$.author.id").value(1));
    }

    @Test
    void testCreateObservationWithInvalidAuthor() throws Exception {
        ObservationDTO observation = new ObservationDTO();
        observation.setName("Test Observation");
        observation.setObservationTime(LocalDateTime.now());
        observation.setAuthorId(999L);

        mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observation)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetObservationById() throws Exception {
        ObservationDTO observation = new ObservationDTO();
        observation.setName("Get Test");
        observation.setObservationTime(LocalDateTime.now());
        observation.setAuthorId(1L);

        String createResponse = mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observation)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ObservationDTO created = objectMapper.readValue(createResponse, ObservationDTO.class);

        mockMvc.perform(get("/api/observation/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.author").exists());
    }

    @Test
    void testUpdateObservation() throws Exception {
        ObservationDTO observation = new ObservationDTO();
        observation.setName("Original Name");
        observation.setObservationTime(LocalDateTime.now());
        observation.setAuthorId(1L);

        String createResponse = mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observation)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ObservationDTO created = objectMapper.readValue(createResponse, ObservationDTO.class);
        created.setName("Updated Name");
        created.setDescription("New description");

        mockMvc.perform(put("/api/observation/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.description").value("New description"));
    }

    @Test
    void testDeleteObservation() throws Exception {
        ObservationDTO observation = new ObservationDTO();
        observation.setName("Delete Test");
        observation.setObservationTime(LocalDateTime.now());
        observation.setAuthorId(1L);

        String createResponse = mockMvc.perform(post("/api/observation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observation)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ObservationDTO created = objectMapper.readValue(createResponse, ObservationDTO.class);

        mockMvc.perform(delete("/api/observation/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/observation/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListObservationsWithPagination() throws Exception {
        ObservationFilterRequest request = new ObservationFilterRequest();
        request.setPage(1);
        request.setSize(10);

        mockMvc.perform(post("/api/observation/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.totalPages").exists());
    }
}
