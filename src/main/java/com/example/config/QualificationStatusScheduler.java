package com.example.config;

import com.example.service.MerchantQualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 资质状态定时更新任务
 * 每天凌晨2点自动更新所有资质的过期状态
 */
@Component
public class QualificationStatusScheduler {

    @Autowired
    private MerchantQualificationService qualificationService;

    /**
     * 每天凌晨2点执行状态更新
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateQualificationStatus() {
        try {
            qualificationService.updateExpiredStatus();
        } catch (Exception e) {
            System.err.println("资质状态更新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
