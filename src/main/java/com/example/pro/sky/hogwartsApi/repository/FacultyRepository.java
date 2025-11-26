package com.example.pro.sky.hogwartsApi.repository;

import com.example.pro.sky.hogwartsApi.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColorIgnoreCase(String color);

    Faculty findByName(String name);
}