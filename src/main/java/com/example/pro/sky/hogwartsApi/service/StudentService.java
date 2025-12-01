package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name: {}, age: {}", student.getName(), student.getAge());

        Student savedStudent = studentRepository.save(student);
        logger.info("Student created with id: {}", savedStudent.getId());
        return savedStudent;
    }

    public Student getStudentById(Long id) {
        logger.info("Was invoked method for get student by id: {}", id);
        logger.debug("Looking for student with id: {}", id);

        checkStudentExists(id);
        Student student = studentRepository.findById(id).get();
        logger.info("Student found: {}", student.getName());
        return student;
    }

    public Optional<Student> findById(Long id) {
        logger.info("Was invoked method for find student by id: {}", id);
        logger.debug("Searching for student with id: {}", id);

        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method for update student with id: {}", id);
        logger.debug("Updating student id: {} with data: {}", id, student);

        checkStudentExists(id);
        student.setId(id);
        Student updatedStudent = studentRepository.save(student);
        logger.info("Student with id: {} updated successfully", id);
        return updatedStudent;
    }

    public void deleteStudent(Long studentId) {
        logger.info("Was invoked method for delete student with id: {}", studentId);
        logger.debug("Attempting to delete student with id: {}", studentId);

        checkStudentExists(studentId);
        studentRepository.deleteById(studentId);
        logger.info("Student with id: {} deleted successfully", studentId);
    }

    public boolean deleteById(Long id) {
        logger.info("Was invoked method for delete student by id: {}", id);

        if (studentRepository.existsById(id)) {
            logger.debug("Deleting student with id: {}", id);
            studentRepository.deleteById(id);
            logger.info("Student with id: {} deleted", id);
            return true;
        }

        logger.warn("Attempted to delete non-existent student with id: {}", id);
        return false;
    }

    public Collection<Student> findStudentByAge(int studentAge) {
        logger.info("Was invoked method for find students by age: {}", studentAge);
        logger.debug("Searching for students with age: {}", studentAge);

        Collection<Student> students = studentRepository.findByAge(studentAge);
        logger.info("Found {} students with age: {}", students.size(), studentAge);
        return students;
    }

    public List<Student> findAll() {
        logger.info("Was invoked method for get all students");

        List<Student> students = studentRepository.findAll();
        logger.info("Found {} students total", students.size());
        logger.debug("Students list: {}", students);
        return students;
    }

    public List<Student> findByNameContaining(String name) {
        logger.info("Was invoked method for find students by name containing: {}", name);
        logger.debug("Searching for students with name containing: {}", name);

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Empty name parameter provided for search");
            throw new IllegalArgumentException("Name parameter cannot be empty");
        }

        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        logger.info("Found {} students with name containing: {}", students.size(), name);
        return students;
    }

    public Long getStudentsCount() {
        logger.info("Was invoked method for get students count");

        Long count = studentRepository.countAllStudents();
        logger.info("Total students count: {}", count);
        return count;
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");

        Double average = studentRepository.findAverageAge();
        Double result = average != null ? average : 0.0;
        logger.info("Average age of students: {}", result);
        return result;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");

        List<Student> students = studentRepository.findLastFiveStudents();
        logger.info("Found {} last students", students.size());
        logger.debug("Last five students: {}", students);
        return students;
    }


    /**
     * Получить имена студентов, начинающиеся с буквы 'А'
     * Отсортированные по алфавиту в верхнем регистре
     */
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with 'A'");

        List<String> names = studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && !name.isEmpty() &&
                        name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        logger.info("Found {} student names starting with 'A'", names.size());
        logger.debug("Names: {}", names);
        return names;
    }

    /**
     * Получить средний возраст всех студентов через Stream API
     */
    public Double getAverageAgeViaStream() {
        logger.info("Was invoked method for get average age via Stream API");

        Double averageAge = studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);

        logger.info("Average age via Stream API: {}", averageAge);
        return averageAge;
    }

    public void checkStudentExists(Long id) {
        logger.debug("Checking if student exists with id: {}", id);

        if (!studentRepository.existsById(id)) {
            logger.error("There is not student with id = {}", id);
            throw new NotFountException("Error: Студент с id " + id + " не найден");
        }

        logger.debug("Student with id: {} exists", id);
    }
}