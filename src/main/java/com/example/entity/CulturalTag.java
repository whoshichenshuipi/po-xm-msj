package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 文化内容标签
 */
@Entity
@Table(name = "cultural_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 200)
    private String description;

    @Size(max = 7)
    private String color = "0";
            // 默认蓝色

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer usageCount = 0;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean enabled = true;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder = 0;

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
