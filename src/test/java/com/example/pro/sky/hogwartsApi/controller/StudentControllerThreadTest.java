package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerThreadTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testPrintStudentNamesParallel() throws Exception {
        // Подготовка данных
        List<Student> students = Arrays.asList(
                createStudent("Harry Potter", 11),
                createStudent("Hermione Granger", 11),
                createStudent("Ron Weasley", 11),
                createStudent("Draco Malfoy", 11),
                createStudent("Neville Longbottom", 11),
                createStudent("Luna Lovegood", 11)
        );

        when(studentService.findAll()).thenReturn(students);
        doNothing().when(studentService).printStudentNamesParallel();

        // Выполнение и проверка
        mockMvc.perform(get("/students/print-parallel"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).printStudentNamesParallel();
    }

    @Test
    void testPrintStudentNamesSynchronized() throws Exception {
        // Подготовка данных
        List<Student> students = Arrays.asList(
                createStudent("Harry Potter", 11),
                createStudent("Hermione Granger", 11),
                createStudent("Ron Weasley", 11),
                createStudent("Draco Malfoy", 11),
                createStudent("Neville Longbottom", 11),
                createStudent("Luna Lovegood", 11)
        );

        when(studentService.findAll()).thenReturn(students);
        doNothing().when(studentService).printStudentNamesSynchronized();

        // Выполнение и проверка
        mockMvc.perform(get("/students/print-synchronized"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).printStudentNamesSynchronized();
    }

    @Test
    void testPrintStudentNamesParallelWithInsufficientStudents() throws Exception {
        // Подготовка данных (менее 6 студентов)
        List<Student> students = Arrays.asList(
                createStudent("Harry Potter", 11),
                createStudent("Hermione Granger", 11)
        );

        when(studentService.findAll()).thenReturn(students);
        doNothing().when(studentService).printStudentNamesParallel();

        // Выполнение и проверка
        mockMvc.perform(get("/students/print-parallel"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).printStudentNamesParallel();
    }

    private Student createStudent(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        return student;
    }
}