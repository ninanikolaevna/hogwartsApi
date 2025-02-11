package com.example.pro.sky.hogwartsApi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Table(name = "faculty")
@Setter
@Entity
@EqualsAndHashCode
@ToString
public class Faculty {

    public Faculty() {
    }

    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Schema
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String color;


}
