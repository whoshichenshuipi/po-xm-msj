package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("merchant")
public class Merchant {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    private String contactPhone;
    private String address;

    // 商户文化定位，如：非遗技艺、老字号
    private String culturePositioning;

    private String description;

    private Boolean approved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


