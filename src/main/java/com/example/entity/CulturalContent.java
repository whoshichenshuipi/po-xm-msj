package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 文化内容（食品/民俗/技艺等）
 */
@Entity
@Table(name = "cultural_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @Size(max = 2000)
    private String summary;

    @Lob
    private String content;

    @NotNull(message = "文化内容类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CulturalType type;

    // 逗号分隔标签，如：非遗技艺,老字号
    @Size(max = 500)
    private String tags;

    // 关联商户ID（后续商户实体建立后使用关联，这里先保存数值）
    private Long merchantId;

    // 关联分类ID
    private Long categoryId;

    // 审核状态（兼容旧字段）
    @Column(nullable = false)
    private Boolean approved = false;

    // 审核状态枚举
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    // 审核人ID
    private Long approverId;

    // 审核时间
    private LocalDateTime approvalTime;

    // 审核意见
    @Size(max = 1000)
    private String approvalComment;

    // 提交人ID
    private Long submitterId;

    // 浏览次数
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;

    // 点赞次数
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer likeCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (approvalStatus == null) {
            approvalStatus = ApprovalStatus.PENDING;
        }
        if (viewCount == null) {
            viewCount = 0;
        }
        if (likeCount == null) {
            likeCount = 0;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 审核状态枚举
     */
    public enum ApprovalStatus {
        PENDING("待审核"),
        APPROVED("已通过"),
        REJECTED("已拒绝");

        private final String description;

        ApprovalStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}


