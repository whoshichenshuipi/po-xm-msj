package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.SystemLog;
import com.example.mapper.SystemLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SystemLogService {

    @Autowired
    private SystemLogMapper mapper;

    public SystemLog create(SystemLog log) {
        log.setId(null);
        if (log.getCreatedAt() == null) log.setCreatedAt(LocalDateTime.now());
        mapper.insert(log);
        return log;
    }

    public Page<SystemLog> page(Long userId, String module, String result, int pageNo, int pageSize) {
        QueryWrapper<SystemLog> qw = new QueryWrapper<>();
        if (userId != null) qw.eq("user_id", userId);
        if (module != null && module.trim().length() > 0) qw.eq("module", module);
        if (result != null && result.trim().length() > 0) qw.eq("result", result);
        qw.orderByDesc("created_at");
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}
