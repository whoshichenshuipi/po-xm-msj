package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Notification;
import com.example.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationMapper mapper;

    public Notification create(Notification n) {
        n.setId(null);
        if (n.getStatus() == null) n.setStatus("UNREAD");
        mapper.insert(n);
        return n;
    }

    public Notification markAsRead(Long id) {
        Notification n = mapper.selectById(id);
        if (n != null) {
            n.setStatus("READ");
            mapper.updateById(n);
        }
        return n;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Page<Notification> page(Long targetId, String type, String status, int pageNo, int pageSize) {
        QueryWrapper<Notification> qw = new QueryWrapper<>();
        if (targetId != null) qw.eq("target_id", targetId);
        if (type != null && type.trim().length() > 0) qw.eq("type", type);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        qw.orderByDesc("created_at");
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}
