package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.service.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("faculty")
@RestController
@AllArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @GetMapping("{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyService.getFacultyById(facultyId);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<Collection<Faculty>> getAllFacultyByColor(@PathVariable String color) {
        return ResponseEntity.ok(facultyService.findByColor(color));
    }

    @PutMapping(value = "{facultyId}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long facultyId, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(facultyId, faculty);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{facultyId}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long facultyId) {
        facultyService.checkFacultyExists(facultyId);
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.noContent().build();
    }
}