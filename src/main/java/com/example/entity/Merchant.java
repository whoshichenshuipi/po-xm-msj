package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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

    // 文化资质级别：national(国家级)、provincial(省级)、municipal(市级)
    @TableField("cultural_level")
    private String culturalLevel;
    
    // 文化评级：A/B/C
    @TableField("rating")
    private String rating;
    
    // 老字号认证
    @TableField("heritage_cert")
    private Boolean heritageCert;
    
    // 非遗认证
    @TableField("intangible_heritage")
    private Boolean intangibleHeritage;

    private Boolean approved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


