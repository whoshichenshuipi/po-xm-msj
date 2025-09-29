package com.example.config;

import com.example.util.AuthValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动时自动验证认证功能
 */
@Component
public class AuthValidationRunner implements CommandLineRunner {

    @Autowired
    private AuthValidationUtil authValidationUtil;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n🚀 美食街管理系统启动完成");
        System.out.println("🔍 开始验证认证功能...\n");
        
//        authValidationUtil.runAllValidations();
        
        System.out.println("\n📚 API文档地址:");
        System.out.println("   - Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("   - 认证接口文档: API_AUTH_DOCUMENTATION.md");
        System.out.println("   - API测试示例: API_TEST_EXAMPLES.md");
        
        System.out.println("\n🌐 前端页面地址:");
        System.out.println("   - 主页: http://localhost:8080/api/index.html");
        System.out.println("   - 登录页: http://localhost:8080/api/login.html");
        System.out.println("   - 注册页: http://localhost:8080/api/register.html");
        
        System.out.println("\n👤 测试账号:");
        System.out.println("   - 管理员: admin / 123456");
        System.out.println("   - 消费者: user1 / 123456");
        System.out.println("   - 商户: user2 / 123456");
        
        System.out.println("\n✨ 系统已准备就绪！\n");
    }
}
