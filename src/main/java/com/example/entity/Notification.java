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
@TableName("notification")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String type; // SYSTEM/MERCHANT/CONSUMER
    private String targetType; // USER/MERCHANT/ALL
    private Long targetId;
    private String status; // UNREAD/READ
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
