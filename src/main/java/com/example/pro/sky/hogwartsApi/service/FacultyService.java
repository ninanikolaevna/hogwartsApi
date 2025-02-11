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

//    FacultyRepository facultyRepository;
FacultyRepository facultyRepository;

    public Faculty createFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long id) {
        checkFacultyExists(id);
        return facultyRepository.findById(id).get(id);
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        checkFacultyExists(id);
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long facultyId) {
        checkFacultyExists(facultyId);
        facultyRepository.deleteById(facultyId);
    }

    public List<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public void checkFacultyExists(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new NotFountException("Error: Факультет с id " + id + " не найден");
        }
    }
}

