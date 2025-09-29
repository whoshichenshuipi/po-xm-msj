package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 培训证书实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("training_certificate")
public class TrainingCertificate {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 培训计划ID
     */
    private Long trainingId;

    /**
     * 培训主题
     */
    private String trainingTitle;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 参与者姓名
     */
    private String participant;

    /**
     * 证书文件URL
     */
    private String certificateUrl;

    /**
     * 证书编号
     */
    private String certificateNo;

    /**
     * 颁发日期
     */
    private LocalDate issueDate;

    /**
     * 状态：DRAFT(草稿), ISSUED(已颁发)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

