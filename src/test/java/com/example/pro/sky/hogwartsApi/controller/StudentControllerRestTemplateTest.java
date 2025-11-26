package com.example.pro.sky.hogwartsApi.controller;


import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/student";
        studentRepository.deleteAll();
    }

    @Test
    void createStudent_shouldReturnStudent() {
        // Given
        Student student = new Student("Harry Potter", 14);

        // When
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Harry Potter", response.getBody().getName());
        assertEquals(14, response.getBody().getAge());
    }

    @Test
    void getStudent_shouldReturnStudent() {
        // Given
        Student savedStudent = studentRepository.save(new Student("Hermione Granger", 15));
        Long studentId = savedStudent.getId();

        // When
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + studentId, Student.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hermione Granger", response.getBody().getName());
    }

    @Test
    void getStudent_notFound() {
        // When
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/999", Student.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() {
        // Given
        Student savedStudent = studentRepository.save(new Student("Ron Weasley", 14));
        Long studentId = savedStudent.getId();
        Student updatedStudent = new Student("Ron Weasley Updated", 15);

        // When
        restTemplate.put(baseUrl + "/" + studentId, updatedStudent);
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + studentId, Student.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ron Weasley Updated", response.getBody().getName());
        assertEquals(15, response.getBody().getAge());
    }

    @Test
    void deleteStudent_shouldRemoveStudent() {
        // Given
        Student savedStudent = studentRepository.save(new Student("Draco Malfoy", 14));
        Long studentId = savedStudent.getId();

        // When
        restTemplate.delete(baseUrl + "/" + studentId);
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + studentId, Student.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getStudentsByAge_shouldReturnFilteredStudents() {
        // Given
        studentRepository.save(new Student("Student1", 14));
        studentRepository.save(new Student("Student2", 15));
        studentRepository.save(new Student("Student3", 14));

        // When
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/age/14",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().stream().allMatch(student -> student.getAge() == 14));
    }

    @Test
    void findByAgeBetween_shouldReturnStudentsInRange() {
        // Given
        studentRepository.save(new Student("Student1", 14));
        studentRepository.save(new Student("Student2", 16));
        studentRepository.save(new Student("Student3", 18));

        // When
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/age-between?min=15&max=17",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Student2", response.getBody().get(0).getName());
    }
}