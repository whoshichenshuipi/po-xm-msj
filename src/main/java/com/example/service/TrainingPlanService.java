package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.TrainingPlan;
import com.example.mapper.TrainingPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 培训计划服务
 * 
 * @author example
 * @since 1.0.0
 */
@Service
@Transactional
public class TrainingPlanService {

    @Autowired
    private TrainingPlanMapper mapper;

    /**
     * 创建培训计划
     */
    public TrainingPlan create(TrainingPlan training) {
        training.setId(null);
        if (training.getStatus() == null) {
            training.setStatus("PUBLISHED");
        }
        if (training.getRegisteredCount() == null) {
            training.setRegisteredCount(0);
        }
        mapper.insert(training);
        return training;
    }

    /**
     * 更新培训计划
     */
    public TrainingPlan update(TrainingPlan training) {
        mapper.updateById(training);
        return training;
    }

    /**
     * 删除培训计划
     */
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    /**
     * 根据ID查询培训计划
     */
    public TrainingPlan findById(Long id) {
        return mapper.selectById(id);
    }

    /**
     * 查询培训计划列表
     */
    public List<TrainingPlan> list(String type, String status) {
        QueryWrapper<TrainingPlan> wrapper = new QueryWrapper<>();
        
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        
        wrapper.orderByDesc("created_at");
        
        return mapper.selectList(wrapper);
    }

    /**
     * 报名培训
     */
    public boolean register(Long id) {
        TrainingPlan training = mapper.selectById(id);
        
        if (training == null) {
            throw new RuntimeException("培训计划不存在");
        }
        
        // 检查是否还有名额
        if (training.getRegisteredCount() >= training.getCapacity()) {
            throw new RuntimeException("培训名额已满");
        }
        
        // 增加报名人数
        training.setRegisteredCount(training.getRegisteredCount() + 1);
        mapper.updateById(training);
        
        return true;
    }
}

