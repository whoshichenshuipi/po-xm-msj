package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("activity")
public class Activity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Long merchantId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String status; // DRAFT/PUBLISHED/ENDED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


