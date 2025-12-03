package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFoundException;
import com.example.pro.sky.hogwartsApi.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static com.example.pro.sky.hogwartsApi.service.DataForTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    private StudentService out;

    @BeforeEach
    void setUp() {
        out = new StudentService();
    }

    @Test
        void createStudent_shouldReturnStudentWithId() {
            Student createdStudent = out.createStudent(HARRY);

            assertNotNull(createdStudent);
            assertEquals("Harry", (createdStudent.getName()));
        }

        @Test
            void getStudentById_shouldReturnStudent() {
                Student createdStudent = out.createStudent(HARRY);

                Student getedStudent = out.getStudentById(createdStudent.getId());

                assertNotNull(getedStudent);
                assertEquals(createdStudent.getId(), getedStudent.getId());
                assertEquals("Harry", getedStudent.getName());

            }

            @Test
            void getStudentById_ThrowsExceptionIfStudentNotFound() {
                assertThrows(NotFoundException.class, () -> {
                    out.getStudentById(100L);
                });
            }

            @Test
            void updateStudent() {
                Student createdStudent = out.createStudent(HARRY);

                Student realStudent = out.updateStudent(createdStudent.getId(), RON);

                assertNotNull(realStudent);
                assertEquals("Ron", realStudent.getName());
            }

            @Test
            void updateStudent_shouldThrowNotFoundException() {
                assertThrows(NotFoundException.class, () -> out.updateStudent(999L, HARRY));
            }

            @Test
            void deleteStudent() {
                Student createdStudent = out.createStudent(HARRY);

                out.deleteStudent(createdStudent.getId());

                assertThrows(NotFoundException.class, () -> out.getStudentById(createdStudent.getId()));
            }


            @Test
            void deleteStudent_ThrowsExceptionIfStudentNotFound() {
                assertThrows(NotFoundException.class, () -> {
                    out.deleteStudent(999L);
                });
            }

            @Test
                void getAllStudentByColor() {
                    out.createStudent(HARRY);
                    out.createStudent(RON);
                    out.createStudent(HERMIONE);

                    Collection<Student> Students = out.findStudentByAge(12);

                    assertNotNull(Students);
                    assertEquals(1, Students.size());
                    assertEquals("Harry", Students.iterator().next().getName());
                    assertTrue(Students.stream().allMatch(f -> f.getAge() == 12));
                }
}
