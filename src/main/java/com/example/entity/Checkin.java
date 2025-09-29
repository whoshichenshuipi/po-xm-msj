package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文化打卡实体类
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkin")
@TableName("checkin")
public class Checkin {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String type; // 打卡类型：FOOD_EXPERIENCE, CULTURAL_LEARNING, ACTIVITY_PARTICIPATION, VENUE_VISIT, OTHER

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title; // 打卡标题

    @Size(max = 1000)
    @Column(length = 1000)
    private String content; // 打卡内容

    @Size(max = 200)
    @Column(length = 200)
    private String location; // 打卡地点

    @Column(name = "location_tag", length = 200)
    private String locationTag; // 位置标签(兼容原字段)

    @NotNull
    @Column(name = "checkin_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime checkinTime; // 打卡时间

    @Min(1)
    @Max(5)
    @Column
    private Integer score; // 体验评分 1-5

    @Size(max = 1000)
    @Column(length = 1000)
    private String images; // 相关图片URL，逗号分隔

    @Size(max = 500)
    @Column(length = 500)
    private String tags; // 标签，逗号分隔

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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


