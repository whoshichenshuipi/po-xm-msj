package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.TrainingCertificate;
import com.example.mapper.TrainingCertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训证书服务
 * 
 * @author example
 * @since 1.0.0
 */
@Service
@Transactional
public class TrainingCertificateService {

    @Autowired
    private TrainingCertificateMapper mapper;

    /**
     * 创建证书
     */
    public TrainingCertificate create(TrainingCertificate certificate) {
        certificate.setId(null);
        if (certificate.getStatus() == null) {
            certificate.setStatus("DRAFT");
        }
        if (certificate.getIssueDate() == null && "ISSUED".equals(certificate.getStatus())) {
            certificate.setIssueDate(LocalDate.now());
        }
        mapper.insert(certificate);
        return certificate;
    }

    /**
     * 更新证书
     */
    public TrainingCertificate update(TrainingCertificate certificate) {
        mapper.updateById(certificate);
        return certificate;
    }

    /**
     * 删除证书
     */
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    /**
     * 根据ID查询证书
     */
    public TrainingCertificate findById(Long id) {
        return mapper.selectById(id);
    }

    /**
     * 查询证书列表
     */
    public List<TrainingCertificate> list(Long merchantId) {
        QueryWrapper<TrainingCertificate> wrapper = new QueryWrapper<>();
        
        if (merchantId != null) {
            wrapper.eq("merchant_id", merchantId);
        }
        
        wrapper.orderByDesc("created_at");
        
        return mapper.selectList(wrapper);
    }

    /**
     * 生成证书编号
     */
    public String generateCertificateNo() {
        // 生成格式：CERT + 年月日 + 随机数
        String dateStr = LocalDate.now().toString().replace("-", "");
        String randomStr = String.valueOf((int)(Math.random() * 10000));
        return "CERT" + dateStr + randomStr;
    }

    /**
     * 颁发证书
     */
    public TrainingCertificate issue(TrainingCertificate certificate) {
        if (certificate.getCertificateNo() == null || certificate.getCertificateNo().isEmpty()) {
            certificate.setCertificateNo(generateCertificateNo());
        }
        certificate.setStatus("ISSUED");
        if (certificate.getIssueDate() == null) {
            certificate.setIssueDate(LocalDate.now());
        }
        mapper.updateById(certificate);
        return certificate;
    }
}

