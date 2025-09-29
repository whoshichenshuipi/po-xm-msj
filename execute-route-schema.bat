@echo off
echo 正在创建 route_details 和 route_nodes 表...
mysql -u root food_street ^< src\main\resources\route-details-schema.sql
if %errorlevel%==0 (
    echo 表创建成功！
) else (
    echo 表创建失败！
)
pause

