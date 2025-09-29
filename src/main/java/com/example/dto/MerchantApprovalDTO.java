package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 商户入驻审核DTO
 * 
 * @author example
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantApprovalDTO {

    private Long id;

    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    @NotBlank(message = "商户名称不能为空")
    private String merchantName;

    @NotBlank(message = "申请人不能为空")
    private String applicant;

    private String phone;

    private String email;

    private String address;

    @NotBlank(message = "审核类型不能为空")
    private String approvalType; // BASIC_INFO, CULTURAL_QUALITY, POSITIONING

    private String status; // PENDING, APPROVED, REJECTED

    private Boolean hasBusinessLicense;
    private Boolean hasHealthPermit;
    private Boolean hasHealthCert;

    private String culturalLevel; // NATIONAL, PROVINCIAL, MUNICIPAL
    private Boolean hasHeritageCert;
    private Boolean hasIntangibleHeritage;
    private Boolean hasCulturalDeptCert;

    private String culturalPositioning;
    private String positioningDescription;
    private String category;
    private Integer matchPercentage;

    private String documents; // JSON格式

    private String reviewComment;
    private Long reviewerId;
    private LocalDateTime reviewedAt;
    private LocalDateTime submitTime;
}

