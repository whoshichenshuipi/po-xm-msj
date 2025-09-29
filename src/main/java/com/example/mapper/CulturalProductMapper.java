package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.CulturalProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文化产品Mapper
 * 
 * @author example
 * @since 1.0.0
 */
@Mapper
public interface CulturalProductMapper extends BaseMapper<CulturalProduct> {
}

