package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public Product create(Product p) {
        p.setId(null);
        productMapper.insert(p);
        return p;
    }

    public Product update(Product p) {
        productMapper.updateById(p);
        return p;
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    public Product findById(Long id) {
        return productMapper.selectById(id);
    }

    public Page<Product> page(Long merchantId, String tag, String keyword, Boolean onSale, int pageNo, int pageSize) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        if (merchantId != null) qw.eq("merchant_id", merchantId);
        if (onSale != null) qw.eq("on_sale", onSale);
        if (tag != null && tag.trim().length() > 0) qw.like("culture_tags", tag);
        if (keyword != null && keyword.trim().length() > 0) {
            qw.and(wrapper -> wrapper
                .like("name", keyword)
                .or()
                .like("description", keyword)
            );
        }
        qw.orderByDesc("created_at");
        return productMapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }
}


