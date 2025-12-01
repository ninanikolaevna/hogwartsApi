package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.model.Avatar;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.AvatarRepository;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private static final int DEFAULT_PAGE_SIZE = 10;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student id: {}", studentId);
        logger.debug("Uploading file: {} for student id: {}", file.getOriginalFilename(), studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with id = {} not found for avatar upload", studentId);
                    return new RuntimeException("Student not found");
                });

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        logger.debug("Saving avatar to path: {}", filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudent(student)
                .orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
        logger.info("Avatar uploaded successfully for student id: {}", studentId);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar by student id: {}", studentId);

        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for student id = {}", studentId);
                    return new RuntimeException("Avatar not found");
                });
    }

    public Page<Avatar> getAllAvatars(Integer page, Integer size) {
        logger.info("Was invoked method for get all avatars with pagination");
        logger.debug("Page: {}, Size: {}", page, size);

        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);

        logger.info("Retrieved {} avatars on page {}", avatars.getContent().size(), pageNumber);
        return avatars;
    }

    public Optional<Avatar> findById(Long id) {
        logger.info("Was invoked method for find avatar by id: {}", id);

        return avatarRepository.findById(id);
    }

    public Avatar save(Avatar avatar) {
        logger.info("Was invoked method for save avatar");

        Avatar savedAvatar = avatarRepository.save(avatar);
        logger.info("Avatar saved with id: {}", savedAvatar.getId());
        return savedAvatar;
    }

    public boolean deleteById(Long id) {
        logger.info("Was invoked method for delete avatar by id: {}", id);

        if (avatarRepository.existsById(id)) {
            avatarRepository.deleteById(id);
            logger.info("Avatar with id: {} deleted successfully", id);
            return true;
        }

        logger.warn("Attempted to delete non-existent avatar with id: {}", id);
        return false;
    }

    private String getExtension(String fileName) {
        logger.debug("Getting extension for file: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}