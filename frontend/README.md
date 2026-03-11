# Campus Idle Frontend (Vue3 + Vite + Pinia + Element Plus)

## 1. 安装与启动

```bash
npm install
npm run dev
```

默认地址：`http://localhost:5173`

## 2. 与后端联调

- 后端地址：`http://localhost:8080`
- 本项目 `vite.config.js` 已配置 `/api` 和 `/uploads` 代理到后端。
- 登录成功后会把 token 持久化到 `localStorage`，并由 axios 拦截器自动在 `Authorization` 中携带：
  - `Authorization: Bearer <token>`

## 3. 目录结构

```text
frontend/
├─ src/
│  ├─ api/                # 所有后端接口封装
│  ├─ layout/             # 主布局
│  ├─ router/             # 路由 + 路由守卫
│  ├─ stores/             # Pinia（登录态与用户信息）
│  ├─ utils/              # axios/request、storage
│  └─ views/
│     ├─ auth/            # 登录/注册
│     ├─ item/            # 首页列表、详情、发布、我的物品
│     ├─ exchange/        # 我的申请、收到申请
│     ├─ profile/         # 个人中心
│     └─ admin/           # 管理后台页面
```

## 4. 页面对应后端接口

- 登录/注册：`/api/auth/login`、`/api/auth/register`
- 当前用户：`/api/auth/me`
- 分类管理：`/api/categories`
- 物品模块：`/api/items`、`/api/items/{id}`
- 上传：`/api/files/upload`
- 交换申请：`/api/exchange-requests/*`
- 交换记录：`/api/exchange-records/my`

## 5. 注意事项

- 管理员菜单基于 `roles` 包含 `ROLE_ADMIN` 控制展示。
- 未登录访问业务页会自动跳转 `/login`。
- 若后端返回 401，会自动清空登录态并跳转登录页。
