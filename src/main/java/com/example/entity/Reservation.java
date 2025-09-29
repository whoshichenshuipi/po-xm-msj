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
@TableName("reservation")
public class Reservation {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long merchantId;
    private Long productId;
    private LocalDateTime reserveTime;
    private Integer people;
    private String status; // PENDING/CONFIRMED/CANCELLED/DONE
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


