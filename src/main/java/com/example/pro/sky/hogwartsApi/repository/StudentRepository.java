package com.example.pro.sky.hogwartsApi.repository;

import com.example.pro.sky.hogwartsApi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Существующий метод - используется в StudentService.findStudentByAge()
    Collection<Student> findByAge(int studentAge);

    // Для поиска по имени (используется в StudentService.findByNameContaining())
    List<Student> findByNameContainingIgnoreCase(String name);

    // 1. SQL запрос для получения количества всех студентов
    @Query("SELECT COUNT(s) FROM Student s")
    Long countAllStudents();

    // 2. SQL запрос для получения среднего возраста студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    Double findAverageAge();

    // 3. SQL запрос для получения пяти последних студентов (нативный запрос)
    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}