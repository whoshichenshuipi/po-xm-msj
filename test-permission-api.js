/**
 * 权限接口测试脚本
 * 测试获取用户权限的API接口
 */

const axios = require('axios');

// 配置基础URL
const BASE_URL = 'http://localhost:8080';

// 测试用户数据
const testUsers = [
    {
        username: 'admin',
        password: 'admin123',
        expectedRole: 'ADMIN'
    },
    {
        username: 'merchant1',
        password: 'merchant123',
        expectedRole: 'MERCHANT'
    },
    {
        username: 'consumer1',
        password: 'consumer123',
        expectedRole: 'CONSUMER'
    }
];

/**
 * 测试权限接口
 */
async function testPermissionAPI() {
    console.log('🚀 开始测试权限接口...\n');
    
    for (const user of testUsers) {
        console.log(`📋 测试用户: ${user.username}`);
        console.log('='.repeat(50));
        
        try {
            // 1. 登录用户
            console.log('1️⃣ 用户登录...');
            const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
                username: user.username,
                password: user.password
            });
            
            if (loginResponse.data.token) {
                console.log('✅ 登录成功');
                
                // 获取session cookie
                const cookies = loginResponse.headers['set-cookie'];
                const sessionCookie = cookies ? cookies[0] : '';
                
                // 2. 获取用户权限
                console.log('2️⃣ 获取用户权限...');
                const permissionResponse = await axios.get(`${BASE_URL}/auth/permissions`, {
                    headers: {
                        'Cookie': sessionCookie
                    }
                });
                
                console.log('✅ 权限获取成功');
                console.log('📊 权限信息:');
                console.log(`   用户ID: ${permissionResponse.data.userId}`);
                console.log(`   用户名: ${permissionResponse.data.username}`);
                console.log(`   角色: ${permissionResponse.data.role}`);
                console.log(`   角色名称: ${permissionResponse.data.roleName}`);
                console.log(`   是否管理员: ${permissionResponse.data.isAdmin}`);
                console.log(`   是否商户: ${permissionResponse.data.isMerchant}`);
                console.log(`   是否消费者: ${permissionResponse.data.isConsumer}`);
                console.log(`   权限描述: ${permissionResponse.data.permissionDescription}`);
                
                // 验证角色是否正确
                if (permissionResponse.data.role === user.expectedRole) {
                    console.log('✅ 角色验证通过');
                } else {
                    console.log(`❌ 角色验证失败，期望: ${user.expectedRole}, 实际: ${permissionResponse.data.role}`);
                }
                
            } else {
                console.log('❌ 登录失败');
            }
            
        } catch (error) {
            console.log('❌ 测试失败:', error.response?.data || error.message);
        }
        
        console.log('\n');
    }
    
    // 测试未登录用户
    console.log('📋 测试未登录用户访问权限接口');
    console.log('='.repeat(50));
    
    try {
        const response = await axios.get(`${BASE_URL}/auth/permissions`);
        console.log('❌ 未登录用户应该无法访问权限接口');
    } catch (error) {
        if (error.response?.status === 401) {
            console.log('✅ 未登录用户正确被拒绝访问 (401 Unauthorized)');
        } else {
            console.log('❌ 未登录用户访问权限接口时出现意外错误:', error.response?.data || error.message);
        }
    }
    
    console.log('\n🎉 权限接口测试完成！');
}

/**
 * 测试权限接口的响应格式
 */
async function testPermissionResponseFormat() {
    console.log('\n🔍 测试权限接口响应格式...');
    
    try {
        // 使用管理员账户测试
        const loginResponse = await axios.post(`${BASE_URL}/auth/login`, {
            username: 'admin',
            password: 'admin123'
        });
        
        if (loginResponse.data.token) {
            const cookies = loginResponse.headers['set-cookie'];
            const sessionCookie = cookies ? cookies[0] : '';
            
            const permissionResponse = await axios.get(`${BASE_URL}/auth/permissions`, {
                headers: {
                    'Cookie': sessionCookie
                }
            });
            
            console.log('📋 权限接口响应格式:');
            console.log(JSON.stringify(permissionResponse.data, null, 2));
            
            // 验证响应字段
            const requiredFields = ['userId', 'username', 'role', 'roleName', 'isAdmin', 'isMerchant', 'isConsumer', 'permissionDescription'];
            const missingFields = requiredFields.filter(field => !(field in permissionResponse.data));
            
            if (missingFields.length === 0) {
                console.log('✅ 响应格式正确，包含所有必需字段');
            } else {
                console.log('❌ 响应格式不完整，缺少字段:', missingFields);
            }
        }
    } catch (error) {
        console.log('❌ 测试响应格式失败:', error.response?.data || error.message);
    }
}

// 运行测试
if (require.main === module) {
    testPermissionAPI()
        .then(() => testPermissionResponseFormat())
        .catch(console.error);
}

module.exports = {
    testPermissionAPI,
    testPermissionResponseFormat
};
