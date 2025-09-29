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
@TableName("system_log")
public class SystemLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String action;
    private String module;
    private String ipAddress;
    private String userAgent;
    private String result; // SUCCESS/FAIL
    private LocalDateTime createdAt;
}
