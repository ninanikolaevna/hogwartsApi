package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFoundException;
import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.repository.FacultyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private Faculty testFaculty;
    private Faculty anotherFaculty;

    @BeforeEach
    void setUp() {
        testFaculty = new Faculty(1L, "Gryffindor", "Red");
        anotherFaculty = new Faculty(2L, "Slytherin", "Green");
    }

    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        // Arrange
        Faculty newFaculty = new Faculty("Gryffindor", "Red");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);

        // Act
        Faculty result = facultyService.createFaculty(newFaculty);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gryffindor", result.getName());
        assertEquals("Red", result.getColor());
        verify(facultyRepository, times(1)).save(newFaculty);
    }

    @Test
    void getFacultyById_shouldReturnFaculty_whenExists() {
        // Arrange
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(testFaculty));

        // Act
        Faculty result = facultyService.getFacultyById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gryffindor", result.getName());
        assertEquals("Red", result.getColor());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void getFacultyById_shouldThrowException_whenNotFound() {
        // Arrange
        when(facultyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> facultyService.getFacultyById(999L));
        verify(facultyRepository, times(1)).findById(999L);
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty_whenExists() {
        // Arrange
        Faculty updatedFaculty = new Faculty(1L, "Gryffindor Updated", "Scarlet");
        when(facultyRepository.existsById(1L)).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        // Act
        Faculty result = facultyService.updateFaculty(1L, updatedFaculty);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gryffindor Updated", result.getName());
        assertEquals("Scarlet", result.getColor());
        verify(facultyRepository, times(1)).existsById(1L);
        verify(facultyRepository, times(1)).save(updatedFaculty);
    }

    @Test
    void updateFaculty_shouldThrowException_whenNotFound() {
        // Arrange
        Faculty updatedFaculty = new Faculty(999L, "Non-existent", "Black");
        when(facultyRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> facultyService.updateFaculty(999L, updatedFaculty));
        verify(facultyRepository, times(1)).existsById(999L);
        verify(facultyRepository, never()).save(any(Faculty.class));
    }

    @Test
    void deleteFaculty_shouldDelete_whenExists() {
        // Arrange
        when(facultyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(facultyRepository).deleteById(1L);

        // Act
        facultyService.deleteFaculty(1L);

        // Assert
        verify(facultyRepository, times(1)).existsById(1L);
        verify(facultyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFaculty_shouldThrowException_whenNotFound() {
        // Arrange
        when(facultyRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> facultyService.deleteFaculty(999L));
        verify(facultyRepository, times(1)).existsById(999L);
        verify(facultyRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByColor_shouldReturnFaculties() {
        // Arrange
        List<Faculty> faculties = Arrays.asList(testFaculty);
        when(facultyRepository.findByColorIgnoreCase("Red")).thenReturn(faculties);

        // Act
        List<Faculty> result = facultyService.findByColor("Red");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Gryffindor", result.get(0).getName());
        assertEquals("Red", result.get(0).getColor());
        verify(facultyRepository, times(1)).findByColorIgnoreCase("Red");
    }

    @Test
    void findByColor_shouldReturnEmptyList_whenNoMatches() {
        // Arrange
        when(facultyRepository.findByColorIgnoreCase("Purple")).thenReturn(Arrays.asList());

        // Act
        List<Faculty> result = facultyService.findByColor("Purple");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facultyRepository, times(1)).findByColorIgnoreCase("Purple");
    }

    @Test
    void findByColor_shouldBeCaseInsensitive() {
        // Arrange
        List<Faculty> faculties = Arrays.asList(testFaculty);
        when(facultyRepository.findByColorIgnoreCase("red")).thenReturn(faculties);

        // Act
        List<Faculty> result = facultyService.findByColor("red");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Red", result.get(0).getColor()); // Оригинальный цвет с большой буквы
        verify(facultyRepository, times(1)).findByColorIgnoreCase("red");
    }

    @Test
    void findAll_shouldReturnAllFaculties() {
        // Arrange
        List<Faculty> faculties = Arrays.asList(testFaculty, anotherFaculty);
        when(facultyRepository.findAll()).thenReturn(faculties);

        // Act
        List<Faculty> result = facultyService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testFaculty));
        assertTrue(result.contains(anotherFaculty));
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoFaculties() {
        // Arrange
        when(facultyRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Faculty> result = facultyService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void findByName_shouldReturnFaculty_whenExists() {
        // Arrange
        when(facultyRepository.findByName("Gryffindor")).thenReturn(testFaculty);

        // Act
        Faculty result = facultyService.findByName("Gryffindor");

        // Assert
        assertNotNull(result);
        assertEquals("Gryffindor", result.getName());
        assertEquals("Red", result.getColor());
        verify(facultyRepository, times(1)).findByName("Gryffindor");
    }

    @Test
    void findByName_shouldReturnNull_whenNotFound() {
        // Arrange
        when(facultyRepository.findByName("NonExistent")).thenReturn(null);

        // Act
        Faculty result = facultyService.findByName("NonExistent");

        // Assert
        assertNull(result);
        verify(facultyRepository, times(1)).findByName("NonExistent");
    }

    @Test
    void findByName_shouldBeCaseSensitive() {
        // Arrange
        when(facultyRepository.findByName("gryffindor")).thenReturn(null);
        when(facultyRepository.findByName("Gryffindor")).thenReturn(testFaculty);

        // Act
        Faculty lowerCaseResult = facultyService.findByName("gryffindor");
        Faculty upperCaseResult = facultyService.findByName("Gryffindor");

        // Assert
        assertNull(lowerCaseResult);
        assertNotNull(upperCaseResult);
        assertEquals("Gryffindor", upperCaseResult.getName());
        verify(facultyRepository, times(1)).findByName("gryffindor");
        verify(facultyRepository, times(1)).findByName("Gryffindor");
    }
}