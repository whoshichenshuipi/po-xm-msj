package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("merchant_qualification")
public class MerchantQualification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long merchantId;

    private String title;

    private String level; // national/provincial/municipal

    private String certNo;

    private LocalDate validUntil;

    private String status; // VALID/EXPIRED/PENDING

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 用于前端显示，不映射到数据库
    @TableField(exist = false)
    private String merchantName;
}


