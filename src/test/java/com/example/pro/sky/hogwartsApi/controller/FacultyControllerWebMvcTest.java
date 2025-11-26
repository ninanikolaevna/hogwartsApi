package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    private final Faculty testFaculty = new Faculty(1L, "Gryffindor", "Red");
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyService facultyService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFaculty_shouldReturnFaculty() throws Exception {
        // Given
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(testFaculty);

        // When & Then
        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void getFaculty_shouldReturnFaculty() throws Exception {
        // Given
        when(facultyService.getFacultyById(1L)).thenReturn(testFaculty);

        // When & Then
        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void getFaculty_notFound() throws Exception {
        // Given
        when(facultyService.getFacultyById(999L))
                .thenThrow(new NotFountException("Error: Факультет с id 999 не найден"));

        // When & Then
        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() throws Exception {
        // Given
        Faculty updatedFaculty = new Faculty(1L, "Gryffindor Updated", "Scarlet");
        when(facultyService.updateFaculty(anyLong(), any(Faculty.class))).thenReturn(updatedFaculty);

        // When & Then
        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"))
                .andExpect(jsonPath("$.color").value("Scarlet"));
    }

    @Test
    void deleteFaculty_shouldReturnOk() throws Exception {
        // Given
        doNothing().when(facultyService).deleteFaculty(1L);

        // When & Then
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByColor_shouldReturnFaculties() throws Exception {
        // Given
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Gryffindor", "Red"),
                new Faculty(2L, "Scarlet", "Red")
        );
        when(facultyService.findByColor("Red")).thenReturn(faculties);

        // When & Then
        mockMvc.perform(get("/faculty/color/Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[1].name").value("Scarlet"))
                .andExpect(jsonPath("$[1].color").value("Red"));
    }

    @Test
    void findByColor_shouldReturnEmptyList() throws Exception {
        // Given
        when(facultyService.findByColor("Purple")).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/faculty/color/Purple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}