package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    private static final Long STUDENT_ID = 1L;
    private static final Long NON_EXISTENT_STUDENT_ID = 999L;
    private static final String STUDENT_NAME = "Harry Potter";
    private static final String UPDATED_STUDENT_NAME = "Harry Potter Updated";
    private static final int STUDENT_AGE = 14;
    private static final int UPDATED_STUDENT_AGE = 15;
    private static final int FILTER_AGE = 14;
    private static final int NON_EXISTENT_AGE = 20;
    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 15;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE);

    @Test
    void createStudent_shouldReturnStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    void getStudent_shouldReturnStudent() throws Exception {
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(testStudent);

        mockMvc.perform(get("/student/{id}", STUDENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    void getStudent_notFound() throws Exception {
        when(studentService.getStudentById(NON_EXISTENT_STUDENT_ID))
                .thenThrow(new NotFountException("Error: Студент с id " + NON_EXISTENT_STUDENT_ID + " не найден"));

        mockMvc.perform(get("/student/{id}", NON_EXISTENT_STUDENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        Student updatedStudent = new Student(STUDENT_ID, UPDATED_STUDENT_NAME, UPDATED_STUDENT_AGE);
        when(studentService.updateStudent(anyLong(), any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/student/{id}", STUDENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(UPDATED_STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(UPDATED_STUDENT_AGE));
    }

    @Test
    void deleteStudent_shouldReturnOk() throws Exception {
        doNothing().when(studentService).deleteStudent(STUDENT_ID);

        mockMvc.perform(delete("/student/{id}", STUDENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void findStudentByAge_shouldReturnStudents() throws Exception {
        Collection<Student> students = List.of(
                new Student(STUDENT_ID, "Student1", FILTER_AGE),
                new Student(2L, "Student2", FILTER_AGE)
        );
        when(studentService.findStudentByAge(FILTER_AGE)).thenReturn(students);

        mockMvc.perform(get("/student/age/{age}", FILTER_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(FILTER_AGE))
                .andExpect(jsonPath("$[1].age").value(FILTER_AGE));
    }

    @Test
    void findStudentByAge_shouldReturnEmptyCollection() throws Exception {
        when(studentService.findStudentByAge(NON_EXISTENT_AGE)).thenReturn(List.of());

        mockMvc.perform(get("/student/age/{age}", NON_EXISTENT_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findStudentsWithParams_shouldUseMockMvcRequestBuildersParam() throws Exception {
        Collection<Student> students = List.of(testStudent);

        // Демонстрация использования MockMvcRequestBuilders.param
        mockMvc.perform(get("/student/filter")
                        .param("minAge", String.valueOf(MIN_AGE))
                        .param("maxAge", String.valueOf(MAX_AGE))
                        .param("name", STUDENT_NAME))
                .andExpect(status().isOk());
    }
}