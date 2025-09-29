package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Merchant;
import com.example.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    public Merchant create(Merchant merchant) {
        merchant.setId(null);
        merchant.setApproved(false);
        merchantMapper.insert(merchant);
        return merchant;
    }

    public Merchant update(Merchant merchant) {
        if (merchant.getId() == null) {
            throw new RuntimeException("ID不能为空");
        }
        merchantMapper.updateById(merchant);
        return merchant;
    }

    public void delete(Long id) {
        merchantMapper.deleteById(id);
    }

    public Merchant approve(Long id, boolean approved) {
        Merchant m = merchantMapper.selectById(id);
        if (m == null) {
            throw new RuntimeException("商户不存在，ID: " + id);
        }
        m.setApproved(approved);
        merchantMapper.updateById(m);
        return m;
    }

    public Page<Merchant> page(String keyword, Boolean approved, int pageNo, int pageSize) {
        QueryWrapper<Merchant> qw = new QueryWrapper<>();
        if (keyword != null && keyword.trim().length() > 0) {
            qw.like("name", keyword).or().like("culture_positioning", keyword);
        }
        if (approved != null) {
            qw.eq("approved", approved);
        }
        return merchantMapper.selectPage(new Page<>(pageNo, pageSize), qw);
    }

    public Merchant findById(Long id) {
        return merchantMapper.selectById(id);
    }
}


