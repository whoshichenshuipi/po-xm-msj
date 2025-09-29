package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Activity;
import com.example.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityService {

    @Autowired
    private ActivityMapper mapper;

    public Activity create(Activity a) {
        a.setId(null);
        if (a.getStatus() == null) a.setStatus("DRAFT");
        mapper.insert(a);
        return a;
    }

    public Activity update(Activity a) {
        mapper.updateById(a);
        return a;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Activity findById(Long id) {
        return mapper.selectById(id);
    }

    public Page<Activity> page(Long merchantId, String status, String keyword, int pageNo, int pageSize) {
        QueryWrapper<Activity> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        if (keyword != null && keyword.trim().length() > 0) qw.like("title", keyword).or().like("location", keyword);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


