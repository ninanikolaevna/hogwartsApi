package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.repository.FacultyRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
        facultyRepository.deleteAll();
    }

    @Test
    void createFaculty_shouldReturnFaculty() {
        // Given
        Faculty faculty = new Faculty("Gryffindor", "Red");

        // When
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gryffindor", response.getBody().getName());
        assertEquals("Red", response.getBody().getColor());
    }

    @Test
    void getFaculty_shouldReturnFaculty() {
        // Given
        Faculty savedFaculty = facultyRepository.save(new Faculty("Slytherin", "Green"));
        Long facultyId = savedFaculty.getId();

        // When
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + facultyId, Faculty.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Slytherin", response.getBody().getName());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        // Given
        Faculty savedFaculty = facultyRepository.save(new Faculty("Hufflepuff", "Yellow"));
        Long facultyId = savedFaculty.getId();
        Faculty updatedFaculty = new Faculty("Hufflepuff Updated", "Yellow-Black");

        // When
        restTemplate.put(baseUrl + "/" + facultyId, updatedFaculty);
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + facultyId, Faculty.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hufflepuff Updated", response.getBody().getName());
        assertEquals("Yellow-Black", response.getBody().getColor());
    }

    @Test
    void deleteFaculty_shouldRemoveFaculty() {
        // Given
        Faculty savedFaculty = facultyRepository.save(new Faculty("Ravenclaw", "Blue"));
        Long facultyId = savedFaculty.getId();

        // When
        restTemplate.delete(baseUrl + "/" + facultyId);
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + facultyId, Faculty.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByColor_shouldReturnFacultiesByColor() {
        // Given
        facultyRepository.save(new Faculty("Faculty1", "Red"));
        facultyRepository.save(new Faculty("Faculty2", "Blue"));
        facultyRepository.save(new Faculty("Faculty3", "Red"));

        // When
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/color/Red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().stream().allMatch(faculty -> faculty.getColor().equals("Red")));
    }

    @Test
    void findByNameOrColor_shouldReturnMatchingFaculties() {
        // Given
        facultyRepository.save(new Faculty("Gryffindor", "Red"));
        facultyRepository.save(new Faculty("Slytherin", "Green"));
        facultyRepository.save(new Faculty("Ravenclaw", "Blue"));

        // When
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/filter?color=Red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Gryffindor", response.getBody().get(0).getName());
    }
}