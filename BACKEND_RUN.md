# 后端启动与数据库连接排错（Windows / IntelliJ）

你遇到的错误核心是：

- `Access denied for user 'root'@'localhost'`
- 随后触发 `Unable to determine Dialect without JDBC metadata`

这不是 Spring Security/JWT 代码问题，而是 **MySQL 账号密码不正确导致 JPA 初始化失败**。

---

## 1. 推荐启动方式（用环境变量传数据库配置）

本项目已支持以下变量：

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRE_SECONDS`
- `JWT_ISSUER`
- `UPLOAD_DIR`

### PowerShell 示例

```powershell
$env:DB_URL="jdbc:mysql://127.0.0.1:3306/campus_idle?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="你的真实密码"
mvn spring-boot:run
```

### IntelliJ 运行配置

在 **Run/Debug Configurations -> Environment variables** 中设置：

```text
DB_URL=jdbc:mysql://127.0.0.1:3306/campus_idle?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8
DB_USERNAME=root
DB_PASSWORD=你的真实密码
```

---

## 2. 先确认 MySQL 账号密码可用

```sql
-- 在 mysql 客户端里先测试能否登录
-- 若 root 无法登录，请创建项目专用用户（推荐）
CREATE USER 'campus_idle'@'%' IDENTIFIED BY 'StrongPass@123';
GRANT ALL PRIVILEGES ON campus_idle.* TO 'campus_idle'@'%';
FLUSH PRIVILEGES;
```

然后把 `DB_USERNAME/DB_PASSWORD` 改为该账号。

---

## 3. 初始化数据库

按顺序执行：

1. `sql/schema.sql`
2. `sql/data_init.sql`
3. `sql/migration/V2_category_item_module.sql`
4. `sql/migration/V3_exchange_module.sql`

---

## 4. 你日志中的级联异常说明

- `UnsatisfiedDependencyException`（`jwtAuthenticationFilter` / `sysUserRepository`）只是上层连锁反应；
- 真正根因是最底部 JDBC 连接失败：`Access denied for user`；
- 连接成功后这些 Bean 会正常创建。

---

## 5. 前端联调地址

- 后端：`http://localhost:8080`
- 前端：`frontend/` 目录运行 `npm install && npm run dev`，默认 `http://localhost:5173`
- Vite 已代理 `/api` 和 `/uploads` 到 8080。
