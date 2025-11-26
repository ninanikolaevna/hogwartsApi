package com.example.pro.sky.hogwartsApi.repository;

import com.example.pro.sky.hogwartsApi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    Optional<Student> findById(Long id);
}
