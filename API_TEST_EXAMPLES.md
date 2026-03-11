# 认证 + 分类 + 闲置物品模块接口测试示例

## 0) 登录获取 token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user01","password":"User@123456"}'
```

## 1) 分类查询（公开）
```bash
curl http://localhost:8080/api/categories
```

## 2) 分类新增（管理员）
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryName":"乐器",
    "sortNo": 10,
    "icon": "https://img.example.com/icon.png",
    "status": 1
  }'
```

## 3) 上传物品图片（登录）
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/path/to/item.jpg"
```

## 4) 发布闲置物品（登录）
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "title": "九成新计算器",
    "description": "考试用计算器，功能正常",
    "conditionLevel": 4,
    "expectedItemDesc": "希望换编程书",
    "campusLocation": "图书馆门口",
    "contactInfo": "微信: campus_test",
    "imageUrls": ["/uploads/abc.jpg","/uploads/def.jpg"]
  }'
```

## 5) 编辑物品（仅本人或管理员）
```bash
curl -X PUT http://localhost:8080/api/items/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "title": "九成新计算器（已降价）",
    "description": "成色好，支持当面验货",
    "conditionLevel": 4,
    "expectedItemDesc": "希望换 C++ 书",
    "campusLocation": "宿舍楼下",
    "contactInfo": "微信: campus_test_2",
    "imageUrls": ["/uploads/new1.jpg"]
  }'
```

## 6) 删除物品（仅本人或管理员）
```bash
curl -X DELETE http://localhost:8080/api/items/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 7) 物品详情（公开）
```bash
curl http://localhost:8080/api/items/1
```

## 8) 物品分页 + 关键词 + 分类筛选（公开）
```bash
curl "http://localhost:8080/api/items?pageNum=1&pageSize=10&keyword=计算器&categoryId=1"
```

## 9) 发起交换申请（登录）
```bash
curl -X POST http://localhost:8080/api/exchange-requests \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": 2,
    "message": "我想和你交换",
    "offeredItemDesc": "可换八成新篮球"
  }'
```

## 10) 查看我发起的申请（登录）
```bash
curl "http://localhost:8080/api/exchange-requests/sent?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

## 11) 查看我收到的申请（登录）
```bash
curl "http://localhost:8080/api/exchange-requests/received?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```

## 12) 同意申请（物品拥有者）
```bash
curl -X PUT http://localhost:8080/api/exchange-requests/1/handle \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "action": "AGREE",
    "exchangeLocation": "图书馆门口",
    "note": "今晚7点"
  }'
```

## 13) 拒绝申请（物品拥有者）
```bash
curl -X PUT http://localhost:8080/api/exchange-requests/1/handle \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "action": "REJECT",
    "note": "暂时不需要"
  }'
```

## 14) 取消申请（申请发起者）
```bash
curl -X PUT http://localhost:8080/api/exchange-requests/1/cancel \
  -H "Authorization: Bearer $TOKEN"
```

## 15) 查询我的交换记录（登录）
```bash
curl "http://localhost:8080/api/exchange-records/my?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer $TOKEN"
```
