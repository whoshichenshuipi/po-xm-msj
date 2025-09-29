package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文化内容推荐记录
 */
@Entity
@Table(name = "cultural_recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0.00")
    private Double score = 0.0;

    @Column(length = 50)
    private String reason; // 推荐原因：HOT, SIMILAR, CATEGORY, TAG, RANDOM

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean clicked = false;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean liked = false;

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
