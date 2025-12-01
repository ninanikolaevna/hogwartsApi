package com.example.pro.sky.hogwartsApi.controller;

import com.example.pro.sky.hogwartsApi.model.Avatar;
import com.example.pro.sky.hogwartsApi.service.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avatars")
@Tag(name = "Avatar Management", description = "APIs for managing avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    @Operation(summary = "Get all avatars with pagination")
    public Page<Avatar> getAllAvatars(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return avatarService.getAllAvatars(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get avatar by ID")
    public Avatar getAvatarById(@PathVariable Long id) {
        return avatarService.findById(id)
                .orElseThrow(() -> new RuntimeException("Avatar not found"));
    }

    @PostMapping
    @Operation(summary = "Create a new avatar")
    public Long createAvatar(@RequestBody Avatar avatar) {
        Avatar savedAvatar = avatarService.save(avatar);
        return savedAvatar.getId();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete avatar by ID")
    public void deleteAvatar(@PathVariable Long id) {
        if (!avatarService.deleteById(id)) {
            throw new RuntimeException("Avatar not found");
        }
    }
}