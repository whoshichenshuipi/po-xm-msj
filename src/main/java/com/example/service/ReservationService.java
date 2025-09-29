package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Reservation;
import com.example.mapper.ReservationMapper;
import com.example.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationMapper mapper;

    public Reservation create(Reservation r) {
        r.setId(null);
        
        // 从Session中获取当前用户ID
        Long currentUserId = PermissionUtil.getCurrentUserId();
        if (currentUserId != null) {
            r.setUserId(currentUserId);
        }
        
        r.setStatus(r.getStatus() == null ? "PENDING" : r.getStatus());
        mapper.insert(r);
        return r;
    }

    public Reservation update(Reservation r) {
        mapper.updateById(r);
        return r;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Reservation findById(Long id) {
        return mapper.selectById(id);
    }

    public Page<Reservation> page(Long userId, Long merchantId, String status, int pageNo, int pageSize) {
        QueryWrapper<Reservation> qw = new QueryWrapper<>();
        if (userId != null) qw.eq("user_id", userId);
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


