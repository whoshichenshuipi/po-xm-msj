package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Review;
import com.example.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewMapper mapper;

    public Review create(Review r) {
        r.setId(null);
        mapper.insert(r);
        return r;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }

    public Page<Review> page(Long merchantId, Long productId, Long userId, int pageNo, int pageSize) {
        QueryWrapper<Review> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (productId != null) qw.eq("product_id", productId);
        if (userId != null) qw.eq("user_id", userId);
        return mapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


