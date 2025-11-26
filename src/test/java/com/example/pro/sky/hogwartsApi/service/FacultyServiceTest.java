//package com.example.pro.sky.hogwartsApi.service;
//
//import com.example.pro.sky.hogwartsApi.exception.NotFountException;
//import com.example.pro.sky.hogwartsApi.model.Faculty;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collection;
//
//import static com.example.pro.sky.hogwartsApi.service.DataForTest.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FacultyServiceTest {
//    private FacultyService out;
//
//    @BeforeEach
//    void setUp() {
//        out = new FacultyService();
//    }
//
//    @Test
//    void createFaculty_shouldReturnFacultyWithId() {
//        Faculty createdFaculty = out.createFaculty(GRYFFINDOR);
//        assertNotNull(createdFaculty);
//        assertEquals("Gryffindor", (createdFaculty.getName()));
//    }
//
//    @Test
//    void getFacultyById_shouldReturnFaculty() {
//        Faculty createdFaculty = out.createFaculty(GRYFFINDOR);
//        Faculty getedFaculty = out.getFacultyById(createdFaculty.getId());
//        assertNotNull(getedFaculty);
//        assertEquals(createdFaculty.getId(), getedFaculty.getId());
//        assertEquals("Gryffindor", getedFaculty.getName());
//    }
//
//    @Test
//    void getFacultyById_ThrowsExceptionIfFacultyNotFound() {
//        assertThrows(NotFountException.class, () -> {
//            out.getFacultyById(100L);
//        });
//    }
//
//    @Test
//    void updateFaculty() {
//        Faculty createdFaculty = out.createFaculty(GRYFFINDOR);
//        Faculty realFaculty = out.updateFaculty(createdFaculty.getId(), SLYTHERIN);
//        assertNotNull(realFaculty);
//        assertEquals("Slytherin", realFaculty.getName());
//    }
//
//    @Test
//    void updateFaculty_shouldThrowNotFoundException() {
//        assertThrows(NotFountException.class, () -> out.updateFaculty(999L, GRYFFINDOR));
//    }
//
//    @Test
//    void deleteFaculty() {
//        Faculty createdFaculty = out.createFaculty(GRYFFINDOR);
//        out.deleteFaculty(createdFaculty.getId());
//        assertThrows(NotFountException.class, () -> out.getFacultyById(createdFaculty.getId()));
//    }
//
//    @Test
//    void deleteFaculty_ThrowsExceptionIfFacultyNotFound() {
//        assertThrows(NotFountException.class, () -> {
//            out.deleteFaculty(999L);
//        });
//    }
//
//    @Test
//    void getAllFacultyByColor() {
//        out.createFaculty(GRYFFINDOR);
//        out.createFaculty(HUFFLEPUFF);
//        out.createFaculty(SLYTHERIN);
//        Collection<Faculty> facultys = out.getFacultyByColor("Blue");
//        assertNotNull(facultys);
//        assertEquals(1, facultys.size());
//        assertEquals("Slytherin", facultys.iterator().next().getName());
//        assertTrue(facultys.stream().allMatch(f -> f.getColor().equals("Blue")));
//    }
//}
