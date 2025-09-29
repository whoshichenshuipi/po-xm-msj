package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.RevenueRecord;
import com.example.mapper.RevenueRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RevenueService {

    @Autowired
    private RevenueRecordMapper mapper;

    public RevenueRecord create(RevenueRecord r) {
        r.setId(null);
        mapper.insert(r);
        return r;
    }

    public Page<RevenueRecord> page(LocalDate start, LocalDate end, Long merchantId, int pageNo, int pageSize) {
        QueryWrapper<RevenueRecord> qw = new QueryWrapper<>();
        if (start != null) qw.ge("record_date", start);
        if (end != null) qw.le("record_date", end);
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


