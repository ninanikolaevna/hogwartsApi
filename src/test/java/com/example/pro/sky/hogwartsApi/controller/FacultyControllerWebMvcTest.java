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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    private static final Long FACULTY_ID = 1L;
    private static final Long NON_EXISTENT_FACULTY_ID = 999L;
    private static final String FACULTY_NAME = "Gryffindor";
    private static final String UPDATED_FACULTY_NAME = "Gryffindor Updated";
    private static final String FACULTY_COLOR = "Red";
    private static final String UPDATED_FACULTY_COLOR = "Scarlet";
    private static final String FILTER_COLOR = "Red";
    private static final String NON_EXISTENT_COLOR = "Purple";
    private static final String FILTER_NAME = "Gryffindor";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faculty testFaculty = new Faculty(FACULTY_ID, FACULTY_NAME, FACULTY_COLOR);

    @Test
    void createFaculty_shouldReturnFaculty() throws Exception {
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    void getFaculty_shouldReturnFaculty() throws Exception {
        when(facultyService.getFacultyById(FACULTY_ID)).thenReturn(testFaculty);

        mockMvc.perform(get("/faculty/{id}", FACULTY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    void getFaculty_notFound() throws Exception {
        when(facultyService.getFacultyById(NON_EXISTENT_FACULTY_ID))
                .thenThrow(new NotFountException("Error: Факультет с id " + NON_EXISTENT_FACULTY_ID + " не найден"));

        mockMvc.perform(get("/faculty/{id}", NON_EXISTENT_FACULTY_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty(FACULTY_ID, UPDATED_FACULTY_NAME, UPDATED_FACULTY_COLOR);
        when(facultyService.updateFaculty(anyLong(), any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/{id}", FACULTY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATED_FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(UPDATED_FACULTY_COLOR));
    }

    @Test
    void deleteFaculty_shouldReturnOk() throws Exception {
        doNothing().when(facultyService).deleteFaculty(FACULTY_ID);

        mockMvc.perform(delete("/faculty/{id}", FACULTY_ID))
                .andExpect(status().isOk());
    }

    @Test
    void findByColor_shouldReturnFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(FACULTY_ID, FACULTY_NAME, FILTER_COLOR),
                new Faculty(2L, "Scarlet", FILTER_COLOR)
        );
        when(facultyService.findByColor(FILTER_COLOR)).thenReturn(faculties);

        mockMvc.perform(get("/faculty/color/{color}", FILTER_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].color").value(FILTER_COLOR))
                .andExpect(jsonPath("$[1].color").value(FILTER_COLOR));
    }

    @Test
    void findByColor_shouldReturnEmptyList() throws Exception {
        when(facultyService.findByColor(NON_EXISTENT_COLOR)).thenReturn(List.of());

        mockMvc.perform(get("/faculty/color/{color}", NON_EXISTENT_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findFacultiesWithParams_shouldUseMockMvcRequestBuildersParam() throws Exception {
        List<Faculty> faculties = List.of(testFaculty);

        // Демонстрация использования MockMvcRequestBuilders.param
        mockMvc.perform(get("/faculty/filter")
                        .param("name", FILTER_NAME)
                        .param("color", FILTER_COLOR)
                        .param("search", "test"))
                .andExpect(status().isOk());
    }
}