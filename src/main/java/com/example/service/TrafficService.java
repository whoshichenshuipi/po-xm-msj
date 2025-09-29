package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.TrafficStat;
import com.example.mapper.TrafficStatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TrafficService {

    @Autowired
    private TrafficStatMapper mapper;

    public TrafficStat create(TrafficStat t) {
        t.setId(null);
        mapper.insert(t);
        return t;
    }

    public Page<TrafficStat> page(LocalDate start, LocalDate end, Long venueId, int pageNo, int pageSize) {
        QueryWrapper<TrafficStat> qw = new QueryWrapper<>();
        if (start != null) qw.ge("stat_date", start);
        if (end != null) qw.le("stat_date", end);
        if (venueId != null) qw.eq("venue_id", venueId);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


