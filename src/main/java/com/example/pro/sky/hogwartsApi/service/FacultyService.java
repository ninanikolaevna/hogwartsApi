package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.exception.NotFoundException;
import com.example.pro.sky.hogwartsApi.model.Faculty;
import com.example.pro.sky.hogwartsApi.repository.FacultyRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty with name: {}, color: {}", faculty.getName(), faculty.getColor());

        Faculty savedFaculty = facultyRepository.save(faculty);
        logger.info("Faculty created with id: {}", savedFaculty.getId());
        return savedFaculty;
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id: {}", id);

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = {}", id);
                    return new NotFoundException("Error: Факультет с id " + id + " не найден");
                });

        logger.info("Faculty found: {}", faculty.getName());
        return faculty;
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with id: {}", id);

        checkFacultyExists(id);
        faculty.setId(id);

        Faculty updatedFaculty = facultyRepository.save(faculty);
        logger.info("Faculty with id: {} updated successfully", id);
        return updatedFaculty;
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty with id: {}", id);

        checkFacultyExists(id);
        facultyRepository.deleteById(id);
        logger.info("Faculty with id: {} deleted successfully", id);
    }

    public List<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find faculties by color: {}", color);

        List<Faculty> faculties = facultyRepository.findByColorIgnoreCase(color);
        logger.info("Found {} faculties with color: {}", faculties.size(), color);
        return faculties;
    }

    public List<Faculty> findAll() {
        logger.info("Was invoked method for get all faculties");

        List<Faculty> faculties = facultyRepository.findAll();
        logger.info("Found {} faculties total", faculties.size());
        return faculties;
    }

    public Faculty findByName(String name) {
        logger.info("Was invoked method for find faculty by name: {}", name);

        Faculty faculty = facultyRepository.findByName(name);
        if (faculty == null) {
            logger.debug("Faculty with name: {} not found", name);
        } else {
            logger.info("Faculty found with name: {}", name);
        }
        return faculty;
    }

    private void checkFacultyExists(Long id) {
        logger.debug("Checking if faculty exists with id: {}", id);

        if (!facultyRepository.existsById(id)) {
            logger.error("Faculty with id = {} does not exist", id);
            throw new NotFoundException("Error: Факультет с id " + id + " не найден");
        }

        logger.debug("Faculty with id: {} exists", id);
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");

        Optional<String> longestName = facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .filter(name -> name != null && !name.isEmpty())
                .max((name1, name2) -> Integer.compare(name1.length(), name2.length()));

        String result = longestName.orElse("No faculties found");
        logger.info("Longest faculty name: {} (length: {})", result, result.length());
        return result;
    }
}