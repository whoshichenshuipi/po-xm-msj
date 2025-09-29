package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hygiene_check")
public class HygieneCheck {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private LocalDate checkDate;
    private String result; // PASS/FAIL
    private String notes;
    private LocalDate nextCheckDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


