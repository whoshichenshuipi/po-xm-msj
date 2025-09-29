package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.MerchantQualification;
import com.example.mapper.MerchantQualificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MerchantQualificationService {

    @Autowired
    private MerchantQualificationMapper mapper;

    public MerchantQualification create(MerchantQualification q) {
        q.setId(null);
        mapper.insert(q);
        return q;
    }

    public MerchantQualification update(MerchantQualification q) {
        mapper.updateById(q);
        return q;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public MerchantQualification findById(Long id) {
        return mapper.selectById(id);
    }

    public Page<MerchantQualification> page(Long merchantId, String status, int pageNo, int pageSize) {
        QueryWrapper<MerchantQualification> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


