package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Venue;
import com.example.mapper.VenueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VenueService {

    @Autowired
    private VenueMapper mapper;

    public Venue create(Venue v) {
        v.setId(null);
        if (v.getStatus() == null) v.setStatus("IDLE");
        mapper.insert(v);
        return v;
    }

    public Venue update(Venue v) {
        mapper.updateById(v);
        return v;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Page<Venue> page(String type, String status, String keyword, int pageNo, int pageSize) {
        QueryWrapper<Venue> qw = new QueryWrapper<>();
        if (type != null && type.trim().length() > 0) qw.eq("type", type);
        if (status != null && status.trim().length() > 0) qw.eq("status", status);
        if (keyword != null && keyword.trim().length() > 0) qw.like("name", keyword).or().like("description", keyword);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


