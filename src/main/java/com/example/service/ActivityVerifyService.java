package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.ActivityVerify;
import com.example.mapper.ActivityVerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityVerifyService {

    @Autowired
    private ActivityVerifyMapper mapper;

    public ActivityVerify verify(ActivityVerify v) {
        v.setId(null);
        if (v.getStatus() == null) v.setStatus("VERIFIED");
        mapper.insert(v);
        return v;
    }

    public Page<ActivityVerify> page(Long activityId, Long signupId, Long verifierId, int pageNo, int pageSize) {
        QueryWrapper<ActivityVerify> qw = new QueryWrapper<>();
        if (activityId != null) qw.eq("activity_id", activityId);
        if (signupId != null) qw.eq("signup_id", signupId);
        if (verifierId != null) qw.eq("verifier_id", verifierId);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


