package com.example.pro.sky.hogwartsApi.service;

import com.example.pro.sky.hogwartsApi.model.Avatar;
import com.example.pro.sky.hogwartsApi.model.Student;
import com.example.pro.sky.hogwartsApi.repository.AvatarRepository;
import com.example.pro.sky.hogwartsApi.repository.StudentRepository;
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
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    // Константа для размера страницы по умолчанию
    private static final int DEFAULT_PAGE_SIZE = 10;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Создаем директорию если не существует
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        // Сохраняем файл на диск
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        // Сохраняем в БД
        Avatar avatar = avatarRepository.findByStudent(student)
                .orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Avatar not found"));
    }

    // Добавляем метод для получения аватарок с пагинацией
    public Page<Avatar> getAllAvatars(Integer page, Integer size) {
        // Устанавливаем значения по умолчанию, если параметры не переданы
        int pageNumber = page != null && page >= 0 ? page : 0;
        int pageSize = size != null && size > 0 ? size : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return avatarRepository.findAll(pageable);
    }

    // Добавляем метод для поиска аватара по ID
    public Optional<Avatar> findById(Long id) {
        return avatarRepository.findById(id);
    }

    // Добавляем метод для сохранения аватара
    public Avatar save(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    // Добавляем метод для удаления аватара по ID
    public boolean deleteById(Long id) {
        if (avatarRepository.existsById(id)) {
            avatarRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}