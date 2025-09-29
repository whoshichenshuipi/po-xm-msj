package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Merchant;
import com.example.entity.MerchantQualification;
import com.example.mapper.MerchantMapper;
import com.example.mapper.MerchantQualificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MerchantQualificationService {

    @Autowired
    private MerchantQualificationMapper mapper;
    
    @Autowired
    private MerchantMapper merchantMapper;

    public MerchantQualification create(MerchantQualification q) {
        q.setId(null);
        q.setCreatedAt(java.time.LocalDateTime.now());
        q.setUpdatedAt(java.time.LocalDateTime.now());
        if (q.getStatus() == null) {
            q.setStatus("VALID");
        }
        mapper.insert(q);
        return q;
    }

    public MerchantQualification update(MerchantQualification q) {
        q.setUpdatedAt(java.time.LocalDateTime.now());
        mapper.updateById(q);
        return q;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public MerchantQualification findById(Long id) {
        return mapper.selectById(id);
    }

    public Page<MerchantQualification> page(Long merchantId, String status, String level, String keyword, int pageNo, int pageSize) {
        QueryWrapper<MerchantQualification> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        if (level != null && level.trim().length() > 0) qw.eq("level", level);
        if (keyword != null && keyword.trim().length() > 0) {
            qw.and(wrapper -> wrapper
                .like("title", keyword)
                .or()
                .like("cert_no", keyword)
            );
        }
        qw.orderByDesc("created_at");
        
        Page<MerchantQualification> page = mapper.selectPage(new Page<>(pageNo, pageSize), qw);
        
        // 关联查询商户名称
        enrichWithMerchantName(page.getRecords());
        
        return page;
    }
    
    /**
     * 为资质列表补充商户名称
     */
    private void enrichWithMerchantName(List<MerchantQualification> qualifications) {
        if (qualifications == null || qualifications.isEmpty()) {
            return;
        }
        
        // 收集所有的 merchantId
        java.util.Set<Long> merchantIds = qualifications.stream()
            .map(MerchantQualification::getMerchantId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        if (merchantIds.isEmpty()) {
            return;
        }
        
        // 批量查询商户信息
        QueryWrapper<Merchant> merchantQw = new QueryWrapper<>();
        merchantQw.in("id", merchantIds);
        List<Merchant> merchants = merchantMapper.selectList(merchantQw);
        
        // 建立 merchantId -> merchantName 的映射
        Map<Long, String> merchantNameMap = merchants.stream()
            .collect(Collectors.toMap(Merchant::getId, Merchant::getName));
        
        // 设置资质中的商户名称
        for (MerchantQualification q : qualifications) {
            if (q.getMerchantId() != null) {
                q.setMerchantName(merchantNameMap.get(q.getMerchantId()));
            }
        }
    }
    
    /**
     * 更新所有资质的过期状态
     * 根据有效期自动更新状态为 EXPIRED 或 VALID
     */
    public void updateExpiredStatus() {
        LocalDate today = LocalDate.now();
        
        // 更新已过期的资质状态
        UpdateWrapper<MerchantQualification> expiredWrapper = new UpdateWrapper<>();
        expiredWrapper.eq("status", "VALID")
                      .isNotNull("valid_until")
                      .lt("valid_until", today);
        expiredWrapper.set("status", "EXPIRED")
                      .set("updated_at", LocalDateTime.now());
        
        int expiredCount = mapper.update(null, expiredWrapper);
        
        // 更新未过期的资质状态（从 EXPIRED 改回 VALID）
        UpdateWrapper<MerchantQualification> validWrapper = new UpdateWrapper<>();
        validWrapper.eq("status", "EXPIRED")
                   .isNotNull("valid_until")
                   .ge("valid_until", today);
        validWrapper.set("status", "VALID")
                    .set("updated_at", LocalDateTime.now());
        
        int validCount = mapper.update(null, validWrapper);
        
        System.out.println("资质状态更新完成: " + expiredCount + " 个已过期, " + validCount + " 个已恢复有效");
    }
    
    /**
     * 获取指定资质的实时状态
     */
    public String getRealTimeStatus(Long qualificationId) {
        MerchantQualification qualification = mapper.selectById(qualificationId);
        if (qualification == null) {
            return null;
        }
        
        // 根据有效期计算实时状态
        if (qualification.getValidUntil() == null) {
            return "VALID"; // 永久有效
        }
        
        LocalDate today = LocalDate.now();
        if (qualification.getValidUntil().isBefore(today)) {
            return "EXPIRED";
        } else {
            return "VALID";
        }
    }
}


