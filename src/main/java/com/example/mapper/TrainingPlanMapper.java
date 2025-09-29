package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.TrainingPlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 培训计划Mapper
 * 
 * @author example
 * @since 1.0.0
 */
@Mapper
public interface TrainingPlanMapper extends BaseMapper<TrainingPlan> {
}

