package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create a new student")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/age")
    @Operation(summary = "Find students by age")
    public Collection<Student> getAllStudentByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student by ID")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student by ID")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/age-between")
    @Operation(summary = "Find students by age range")
    public Collection<Student> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/faculty/{id}")
    @Operation(summary = "Find faculty by student ID")
    public Object findFacultyByStudentId(@PathVariable Long id) {
        return studentService.findFacultyByStudentId(id);
    }


    @GetMapping("/print-parallel")
    @Operation(summary = "Print student names in parallel threads",
            description = "Prints student names using multiple threads (unsynchronized)")
    public void printStudentNamesParallel() {
        studentService.printStudentNamesParallel();
    }

    @GetMapping("/print-synchronized")
    @Operation(summary = "Print student names in synchronized threads",
            description = "Prints student names using multiple threads with synchronization")
    public void printStudentNamesSynchronized() {
        studentService.printStudentNamesSynchronized();
    }
}