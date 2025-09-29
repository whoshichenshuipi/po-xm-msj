package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.ActivitySignup;
import com.example.mapper.ActivitySignupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivitySignupService {

    @Autowired
    private ActivitySignupMapper mapper;

    public ActivitySignup signup(ActivitySignup s) {
        s.setId(null);
        if (s.getStatus() == null) s.setStatus("PENDING");
        mapper.insert(s);
        return s;
    }

    public ActivitySignup confirm(Long id) {
        ActivitySignup s = mapper.selectById(id);
        if (s == null) throw new RuntimeException("报名不存在: " + id);
        s.setStatus("CONFIRMED");
        mapper.updateById(s);
        return s;
    }

    public void cancel(Long id) {
        mapper.deleteById(id);
    }

    public Page<ActivitySignup> page(Long activityId, Long userId, String status, int pageNo, int pageSize) {
        QueryWrapper<ActivitySignup> qw = new QueryWrapper<>();
        if (activityId != null) qw.eq("activity_id", activityId);
        if (userId != null) qw.eq("user_id", userId);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


