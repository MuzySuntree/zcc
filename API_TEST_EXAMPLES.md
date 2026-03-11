# 认证与用户模块接口测试示例

## 1. 注册
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1001",
    "password": "User@123456",
    "nickname": "测试用户",
    "phone": "13800138000",
    "email": "user1001@example.com"
  }'
```

## 2. 登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1001",
    "password": "User@123456"
  }'
```

> 从返回中取 `data.token`，下方用 `$TOKEN` 代替。

## 3. 获取当前用户
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

## 4. 修改个人资料
```bash
curl -X PUT http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "新昵称",
    "realName": "张三",
    "gender": 1,
    "phone": "13900139000",
    "email": "newmail@example.com",
    "avatarUrl": "http://localhost:8080/static/avatar.png"
  }'
```

## 5. 修改密码
```bash
curl -X PUT http://localhost:8080/api/users/password \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "User@123456",
    "newPassword": "User@12345678"
  }'
```
