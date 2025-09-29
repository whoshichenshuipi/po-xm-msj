package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.HygieneCheck;
import com.example.mapper.HygieneCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HygieneCheckService {

    @Autowired
    private HygieneCheckMapper mapper;

    public HygieneCheck create(HygieneCheck h) {
        h.setId(null);
        mapper.insert(h);
        return h;
    }

    public HygieneCheck update(HygieneCheck h) {
        mapper.updateById(h);
        return h;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Page<HygieneCheck> page(Long merchantId, String result, int pageNo, int pageSize) {
        QueryWrapper<HygieneCheck> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (result != null && result.trim().length() > 0) qw.eq("result", result);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


