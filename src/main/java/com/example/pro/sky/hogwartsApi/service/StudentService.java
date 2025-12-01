package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFountException;
import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private static final int REQUIRED_STUDENTS_COUNT = 6;

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

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = {}", id);
                    return new NotFountException("Error: Студент с id " + id + " не найден");
                });

        logger.info("Student found: {}", student.getName());
        return student;
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method for update student with id: {}", id);

        checkStudentExists(id);
        student.setId(id);

        Student updatedStudent = studentRepository.save(student);
        logger.info("Student with id: {} updated successfully", id);
        return updatedStudent;
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);

        checkStudentExists(id);
        studentRepository.deleteById(id);
        logger.info("Student with id: {} deleted successfully", id);
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age: {}", age);

        Collection<Student> students = studentRepository.findByAge(age);
        logger.info("Found {} students with age: {}", students.size(), age);
        return students;
    }

    public Collection<Student> findAll() {
        logger.info("Was invoked method for get all students");

        Collection<Student> students = studentRepository.findAll();
        logger.info("Found {} students total", students.size());
        return students;
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between {} and {}", min, max);

        Collection<Student> students = studentRepository.findByAgeBetween(min, max);
        logger.info("Found {} students with age between {} and {}", students.size(), min, max);
        return students;
    }

    public Object findFacultyByStudentId(Long id) {
        logger.info("Was invoked method for find faculty by student id: {}", id);

        Student student = getStudentById(id);
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.info("Student with id: {} has no faculty", id);
            return "Student has no faculty";
        }
        logger.info("Faculty found for student id {}: {}", id, faculty.getName());
        return faculty;
    }

    private void checkStudentExists(Long id) {
        logger.debug("Checking if student exists with id: {}", id);

        if (!studentRepository.existsById(id)) {
            logger.error("Student with id = {} does not exist", id);
            throw new NotFountException("Error: Студент с id " + id + " не найден");
        }

        logger.debug("Student with id: {} exists", id);
    }

    // ============ МНОГОПОТОЧНЫЕ МЕТОДЫ ============

    public void printStudentNamesParallel() {
        logger.info("Was invoked method for printing student names in parallel");

        List<Student> students = (List<Student>) findAll();

        if (students.size() < REQUIRED_STUDENTS_COUNT) {
            logger.warn("Need at least {} students, but found only {}", REQUIRED_STUDENTS_COUNT, students.size());
            System.out.println("Need at least " + REQUIRED_STUDENTS_COUNT + " students to demonstrate parallel printing");
            return;
        }

        // Получаем первые 6 студентов
        List<Student> firstSixStudents = students.subList(0, REQUIRED_STUDENTS_COUNT);
        String[] names = firstSixStudents.stream()
                .map(Student::getName)
                .toArray(String[]::new);

        System.out.println("=== Printing student names in parallel ===");

        // Основной поток печатает первые два имени
        System.out.println("Main thread: " + names[0]);
        System.out.println("Main thread: " + names[1]);

        // Первый параллельный поток печатает 3-е и 4-е имя
        Thread thread1 = new Thread(() -> {
            System.out.println("Parallel thread 1: " + names[2]);
            System.out.println("Parallel thread 1: " + names[3]);
        });

        // Второй параллельный поток печатает 5-е и 6-е имя
        Thread thread2 = new Thread(() -> {
            System.out.println("Parallel thread 2: " + names[4]);
            System.out.println("Parallel thread 2: " + names[5]);
        });

        // Запускаем потоки
        thread1.start();
        thread2.start();

        // Ждем завершения потоков
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Parallel printing completed ===");
    }

    public void printStudentNamesSynchronized() {
        logger.info("Was invoked method for printing student names synchronized");

        List<Student> students = (List<Student>) findAll();

        if (students.size() < REQUIRED_STUDENTS_COUNT) {
            logger.warn("Need at least {} students, but found only {}", REQUIRED_STUDENTS_COUNT, students.size());
            System.out.println("Need at least " + REQUIRED_STUDENTS_COUNT + " students to demonstrate synchronized printing");
            return;
        }

        // Получаем первые 6 студентов
        List<Student> firstSixStudents = students.subList(0, REQUIRED_STUDENTS_COUNT);
        String[] names = firstSixStudents.stream()
                .map(Student::getName)
                .toArray(String[]::new);

        System.out.println("=== Printing student names with synchronization ===");

        // Основной поток печатает первые два имени
        printStudentNameSynchronized("Main thread", names[0]);
        printStudentNameSynchronized("Main thread", names[1]);

        // Первый параллельный поток печатает 3-е и 4-е имя
        Thread thread1 = new Thread(() -> {
            printStudentNameSynchronized("Parallel thread 1", names[2]);
            printStudentNameSynchronized("Parallel thread 1", names[3]);
        });

        // Второй параллельный поток печатает 5-е и 6-е имя
        Thread thread2 = new Thread(() -> {
            printStudentNameSynchronized("Parallel thread 2", names[4]);
            printStudentNameSynchronized("Parallel thread 2", names[5]);
        });

        // Запускаем потоки
        thread1.start();
        thread2.start();

        // Ждем завершения потоков
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Synchronized printing completed ===");
    }

    private synchronized void printStudentNameSynchronized(String threadName, String studentName) {
        System.out.println(threadName + " (synchronized): " + studentName);
    }
}