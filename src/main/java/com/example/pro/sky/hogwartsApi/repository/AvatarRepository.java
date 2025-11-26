package com.example.pro.sky.hogwartsApi.repository;


import com.example.pro.sky.hogwartsApi.model.Avatar;
import com.example.pro.sky.hogwartsApi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudent(Student student);

    Optional<Avatar> findByStudentId(Long studentId);
}

