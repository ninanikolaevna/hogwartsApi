package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        checkStudentExists(id);
        return studentRepository.findById(id).get();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student student) {
        checkStudentExists(id);
        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        checkStudentExists(studentId);
        studentRepository.deleteById(studentId);
    }

    public boolean deleteById(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Collection<Student> findStudentByAge(int studentAge) {
        return studentRepository.findByAge(studentAge);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    // Новые методы для дополнительных эндпоинтов
    public Long getStudentsCount() {
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        Double average = studentRepository.findAverageAge();
        return average != null ? average : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }

    public void checkStudentExists(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFountException("Error: Студент с id " + id + " не найден");
        }
    }
}