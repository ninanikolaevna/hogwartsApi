package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.model.Student;

public class DataForTest {

    public static final Student HARRY = new Student(1L, "Harry", 12);
    public static final Student RON = new Student(2L, "Ron", 11);
    public static final Student HERMIONE = new Student(3L, "Hermione", 10);

    public static final Faculty GRYFFINDOR = new Faculty(1L, "Gryffindor", "Red");
    public static final Faculty HUFFLEPUFF = new Faculty(2L, "Hufflepuff", "Yellow");
    public static final Faculty SLYTHERIN = new Faculty(3L, "Slytherin", "Blue");
}
