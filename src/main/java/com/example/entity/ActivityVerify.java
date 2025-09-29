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
@TableName("activity_verify")
public class ActivityVerify {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long signupId;
    private Long verifierId;
    private LocalDateTime verifyTime;
    private String status; // VERIFIED/REJECTED
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


