package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.repository.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new NotFountException("Error: Факультет с id " + id + " не найден"));
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        checkFacultyExists(id);
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        checkFacultyExists(id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty findByName(String name) {
        return facultyRepository.findByName(name);
    }

    private void checkFacultyExists(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new NotFountException("Error: Факультет с id " + id + " не найден");
        }
    }
}