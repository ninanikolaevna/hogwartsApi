package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Create a new student")
    public Long createStudent(@RequestBody Student student) {
        Student savedStudent = studentService.createStudent(student);
        return savedStudent.getId();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
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

    @GetMapping("/filterByAge")
    @Operation(summary = "Find students by age")
    public Collection<Student> findStudentByAge(@RequestParam int age) {
        return studentService.findStudentByAge(age);
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name")
    public List<Student> searchStudentsByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name parameter cannot be empty");
        }
        return studentService.findByNameContaining(name);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total number of students")
    public Long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/average-age")
    @Operation(summary = "Get average age of students")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/last-five")
    @Operation(summary = "Get last five students")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }


    @GetMapping("/names-starting-with-a")
    @Operation(summary = "Get student names starting with 'A'",
            description = "Returns sorted list of student names in uppercase starting with letter 'A'")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age-stream")
    @Operation(summary = "Get average age via Stream API",
            description = "Returns average age of all students calculated using Stream API")
    public Double getAverageAgeViaStream() {
        return studentService.getAverageAgeViaStream();
    }
}