package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据库修复运行器
 * 在应用启动时自动修复用户角色数据
 */
@Component
public class DatabaseFixRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔧 开始检查并修复数据库用户角色数据...");
        
        try {
            // 检查是否有小写的角色数据
            List<Map<String, Object>> lowercaseRoles = jdbcTemplate.queryForList(
                "SELECT COUNT(*) as count FROM users WHERE role IN ('admin', 'merchant', 'consumer')"
            );
            
            int lowercaseCount = ((Number) lowercaseRoles.get(0).get("count")).intValue();
            
            if (lowercaseCount > 0) {
                System.out.println("⚠️ 发现 " + lowercaseCount + " 个小写角色数据，开始修复...");
                
                // 修复小写角色数据
                int adminFixed = jdbcTemplate.update("UPDATE users SET role = 'ADMIN' WHERE role = 'admin'");
                int merchantFixed = jdbcTemplate.update("UPDATE users SET role = 'MERCHANT' WHERE role = 'merchant'");
                int consumerFixed = jdbcTemplate.update("UPDATE users SET role = 'CONSUMER' WHERE role = 'consumer'");
                
                System.out.println("✅ 角色数据修复完成:");
                System.out.println("   - 修复管理员角色: " + adminFixed + " 条");
                System.out.println("   - 修复商户角色: " + merchantFixed + " 条");
                System.out.println("   - 修复消费者角色: " + consumerFixed + " 条");
            } else {
                System.out.println("✅ 用户角色数据正常，无需修复");
            }
            
            // 显示当前用户数据
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT id, username, email, role FROM users ORDER BY id"
            );
            
            System.out.println("📊 当前用户数据:");
            for (Map<String, Object> user : users) {
                System.out.println(String.format("   ID: %s, 用户名: %s, 角色: %s", 
                    user.get("id"), user.get("username"), user.get("role")));
            }
            
        } catch (Exception e) {
            System.err.println("❌ 数据库修复失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🎉 数据库修复检查完成！");
    }
}
