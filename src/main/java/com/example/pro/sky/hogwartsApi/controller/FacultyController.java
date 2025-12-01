package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.service.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
@AllArgsConstructor
@Tag(name = "Faculty Management", description = "APIs for managing faculties")
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @Operation(summary = "Create a new faculty")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get faculty by ID")
    public Faculty getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Find faculties by color")
    public Collection<Faculty> getAllFacultyByColor(@PathVariable String color) {
        return facultyService.findByColor(color);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update faculty by ID")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.updateFaculty(id, faculty);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete faculty by ID")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping
    @Operation(summary = "Get all faculties")
    public List<Faculty> getAllFaculties() {
        return facultyService.findAll();
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find faculty by name")
    public Faculty findByName(@PathVariable String name) {
        return facultyService.findByName(name);
    }

    // ============ НОВЫЙ ЭНДПОИНТ ДЛЯ STREAM API ============

    @GetMapping("/longest-name")
    @Operation(summary = "Get longest faculty name",
            description = "Returns the longest faculty name using Stream API")
    public String getLongestFacultyName() {
        return facultyService.getLongestFacultyName();
    }
}