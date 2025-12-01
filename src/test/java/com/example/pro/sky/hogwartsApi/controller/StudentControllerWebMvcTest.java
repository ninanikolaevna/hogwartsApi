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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    private static final Long STUDENT_ID = 1L;
    private static final Long NON_EXISTENT_STUDENT_ID = 999L;
    private static final String STUDENT_NAME = "Harry Potter";
    private static final String STUDENT_EMAIL = "harry@hogwarts.com";
    private static final String UPDATED_STUDENT_NAME = "Harry Potter Updated";
    private static final String UPDATED_STUDENT_EMAIL = "harry.updated@hogwarts.com";
    private static final int STUDENT_AGE = 14;
    private static final int UPDATED_STUDENT_AGE = 15;
    private static final int FILTER_AGE = 14;
    private static final int NON_EXISTENT_AGE = 20;
    private static final String SEARCH_NAME = "Harry";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Student testStudent = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE, STUDENT_EMAIL);

    @Test
    void createStudent_shouldReturnStudentId() throws Exception {
        // Arrange
        Student newStudent = new Student(null, STUDENT_NAME, STUDENT_AGE, STUDENT_EMAIL);
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);

        // Act & Assert
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(content().string(STUDENT_ID.toString()));
    }

    @Test
    void getStudentById_shouldReturnStudent() throws Exception {
        // Arrange
        when(studentService.getStudentById(STUDENT_ID)).thenReturn(testStudent);

        // Act & Assert
        mockMvc.perform(get("/student/{id}", STUDENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE))
                .andExpect(jsonPath("$.email").value(STUDENT_EMAIL));
    }

    @Test
    void getStudentById_notFound() throws Exception {
        // Arrange
        when(studentService.getStudentById(NON_EXISTENT_STUDENT_ID))
                .thenThrow(new NotFountException("Error: Студент с id " + NON_EXISTENT_STUDENT_ID + " не найден"));

        // Act & Assert
        mockMvc.perform(get("/student/{id}", NON_EXISTENT_STUDENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        // Arrange
        Student updateData = new Student(null, UPDATED_STUDENT_NAME, UPDATED_STUDENT_AGE, UPDATED_STUDENT_EMAIL);
        Student updatedStudent = new Student(STUDENT_ID, UPDATED_STUDENT_NAME, UPDATED_STUDENT_AGE, UPDATED_STUDENT_EMAIL);

        when(studentService.updateStudent(eq(STUDENT_ID), any(Student.class))).thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/student/{id}", STUDENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(UPDATED_STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(UPDATED_STUDENT_AGE))
                .andExpect(jsonPath("$.email").value(UPDATED_STUDENT_EMAIL));
    }

    @Test
    void deleteStudent_shouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(studentService).deleteStudent(STUDENT_ID);

        // Act & Assert
        mockMvc.perform(delete("/student/{id}", STUDENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void findStudentByAge_shouldReturnStudents() throws Exception {
        // Arrange
        Collection<Student> students = Arrays.asList(
                new Student(STUDENT_ID, "Student1", FILTER_AGE, "student1@example.com"),
                new Student(2L, "Student2", FILTER_AGE, "student2@example.com")
        );
        when(studentService.findStudentByAge(FILTER_AGE)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/student/filterByAge")
                        .param("age", String.valueOf(FILTER_AGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(FILTER_AGE))
                .andExpect(jsonPath("$[1].age").value(FILTER_AGE));
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE, STUDENT_EMAIL),
                new Student(2L, "Hermione Granger", 15, "hermione@hogwarts.com")
        );
        when(studentService.findAll()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(STUDENT_NAME))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));
    }

    @Test
    void searchStudentsByName_shouldReturnMatchingStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student(STUDENT_ID, STUDENT_NAME, STUDENT_AGE, STUDENT_EMAIL),
                new Student(2L, "Harry Styles", 20, "harry.styles@example.com")
        );
        when(studentService.findByNameContaining(SEARCH_NAME)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/student/search")
                        .param("name", SEARCH_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name", containsString(SEARCH_NAME)))
                .andExpect(jsonPath("$[1].name", containsString(SEARCH_NAME)));
    }

    @Test
    void searchStudentsByName_withEmptyName_shouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/student/search")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentsCount_shouldReturnTotalCount() throws Exception {
        // Arrange
        when(studentService.getStudentsCount()).thenReturn(25L);

        // Act & Assert
        mockMvc.perform(get("/student/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));
    }

    @Test
    void getAverageAge_shouldReturnAverageAge() throws Exception {
        // Arrange
        when(studentService.getAverageAge()).thenReturn(16.7);

        // Act & Assert
        mockMvc.perform(get("/student/average-age"))
                .andExpect(status().isOk())
                .andExpect(content().string("16.7"));
    }

    @Test
    void getLastFiveStudents_shouldReturnFiveStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student(1L, "Student1", 20, "student1@example.com"),
                new Student(2L, "Student2", 21, "student2@example.com"),
                new Student(3L, "Student3", 22, "student3@example.com"),
                new Student(4L, "Student4", 23, "student4@example.com"),
                new Student(5L, "Student5", 24, "student5@example.com")
        );
        when(studentService.getLastFiveStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/student/last-five"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Student1"))
                .andExpect(jsonPath("$[4].name").value("Student5"));
    }
}