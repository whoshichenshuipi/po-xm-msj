package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("revenue_record")
public class RevenueRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private LocalDate recordDate;
    private BigDecimal revenue;
    private LocalDateTime createdAt;
}


