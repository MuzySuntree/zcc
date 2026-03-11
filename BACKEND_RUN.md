# 后端启动与数据库连接排错（Windows / IntelliJ）

从你给的两段日志看，根因始终是 **MySQL 认证配置**：

- `Access denied for user 'root'@'localhost' (using password: YES)`
- `Access denied for user 'root'@'localhost' (using password: NO)`

这表示应用分别尝试了“带密码/不带密码”连接，但与当前 MySQL 账号策略不匹配。

> 结论：不是 JWT 代码问题，也不是 Repository 注入代码问题；`UnsatisfiedDependencyException` 是 JPA 初始化失败后的连锁异常。

---

## 1) 先确认当前 MySQL 账号真实登录方式

在命令行手工验证：

```bash
mysql -uroot -p
```

- 能登录：说明 root 需要密码；
- 不加 `-p` 能登录：说明 root 可能为空密码；
- 都不行：请重置 root 密码或创建项目专用账号。

推荐创建专用账号：

```sql
CREATE USER 'campus_idle'@'%' IDENTIFIED BY 'StrongPass@123';
GRANT ALL PRIVILEGES ON campus_idle.* TO 'campus_idle'@'%';
FLUSH PRIVILEGES;
```

---

## 2) IntelliJ 里必须配置 Environment Variables（重点）

Run/Debug Configurations -> Environment variables：

```text
DB_URL=jdbc:mysql://127.0.0.1:3306/campus_idle?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8
DB_USERNAME=campus_idle
DB_PASSWORD=StrongPass@123
JWT_SECRET=CampusIdleJwtSecretCampusIdleJwtSecret123456
```

如果你坚持用 root，就把 `DB_USERNAME/DB_PASSWORD` 改成 root 实际值。

---

## 3) 现在项目配置说明

`application.yml` 已支持环境变量覆盖：

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRE_SECONDS`
- `JWT_ISSUER`
- `UPLOAD_DIR`

并且已移除 `hibernate.dialect` 显式配置（Hibernate 6 会自动识别，避免不必要告警）。

---

## 4) SQL 初始化顺序

按顺序执行：

1. `sql/schema.sql`
2. `sql/data_init.sql`
3. `sql/migration/V2_category_item_module.sql`
4. `sql/migration/V3_exchange_module.sql`

---

## 5) 你日志里“报错后又启动成功”怎么理解？

这通常是某次连接尝试失败被记录为 WARN/ERROR，但后续连接重试成功，最终容器完成启动。

所以判断是否真正可用，请以这两点为准：

1. 是否出现 `Started IdlePlatformApplication`；
2. 能否实际访问接口（例如 `/api/auth/login`）。

---

## 6) 前后端联调

- 后端：`http://localhost:8080`
- 前端：`cd frontend && npm install && npm run dev`（默认 `http://localhost:5173`）
- Vite 已代理 `/api` 和 `/uploads` 到后端。
