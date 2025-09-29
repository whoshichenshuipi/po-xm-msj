package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商户入驻审核实体
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("merchant_approval")
public class MerchantApproval {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 审核类型：BASIC_INFO(基础信息), CULTURAL_QUALITY(文化资质), POSITIONING(文化定位)
     */
    private String approvalType;

    /**
     * 审核状态：PENDING(待审核), APPROVED(已通过), REJECTED(已驳回)
     */
    private String status;

    /**
     * 是否有营业执照
     */
    private Boolean hasBusinessLicense;

    /**
     * 是否有卫生许可证
     */
    private Boolean hasHealthPermit;

    /**
     * 是否有健康证
     */
    private Boolean hasHealthCert;

    /**
     * 文化资质等级：NATIONAL(国家级), PROVINCIAL(省级), MUNICIPAL(市级)
     */
    private String culturalLevel;

    /**
     * 是否有老字号认证
     */
    private Boolean hasHeritageCert;

    /**
     * 是否有非遗传承
     */
    private Boolean hasIntangibleHeritage;

    /**
     * 是否有文化部门认定
     */
    private Boolean hasCulturalDeptCert;

    /**
     * 文化定位
     */
    private String culturalPositioning;

    /**
     * 文化定位描述
     */
    private String positioningDescription;

    /**
     * 文化定位类别
     */
    private String category;

    /**
     * 匹配度(0-100)
     */
    private Integer matchPercentage;

    /**
     * 证件材料(JSON格式)
     */
    private String documents;

    /**
     * 审核意见
     */
    private String reviewComment;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewedAt;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

