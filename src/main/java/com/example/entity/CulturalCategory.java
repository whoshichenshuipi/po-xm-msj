package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 文化内容分类
 */
@Entity
@Table(name = "cultural_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 200)
    private String icon;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder = 0;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean enabled = true;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer contentCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
