# 校园闲置物品交换平台（后端）第一阶段设计文档

> 技术栈：**Java 17 + Spring Boot 4.x + Spring Security 7 + JWT + MySQL 8 + Spring Data JPA + Maven + Lombok + Jakarta Validation**
>
> 本阶段仅输出：后端目录结构、数据库设计、实体/DTO/VO设计、API清单、统一返回与异常处理、认证鉴权流程与权限矩阵。

---

## 1. 后端项目目录结构（完整骨架）

```text
campus-idle-platform-backend/
├─ pom.xml
├─ README.md
├─ sql/
│  ├─ schema.sql                  # 建表脚本
│  └─ data_init.sql               # 初始化数据（角色、管理员账号、测试分类）
├─ uploads/                       # 本地文件上传目录（运行时）
└─ src/
   ├─ main/
   │  ├─ java/com/campus/idle/
   │  │  ├─ IdlePlatformApplication.java
   │  │  ├─ common/
   │  │  │  ├─ Result.java
   │  │  │  ├─ ResultCode.java
   │  │  │  └─ PageResult.java
   │  │  ├─ config/
   │  │  │  ├─ CorsConfig.java
   │  │  │  ├─ JacksonConfig.java
   │  │  │  ├─ JpaAuditingConfig.java
   │  │  │  ├─ SecurityConfig.java
   │  │  │  └─ WebMvcConfig.java
   │  │  ├─ security/
   │  │  │  ├─ JwtAuthenticationFilter.java
   │  │  │  ├─ JwtAuthenticationEntryPoint.java
   │  │  │  ├─ JwtAccessDeniedHandler.java
   │  │  │  ├─ CustomUserDetails.java
   │  │  │  ├─ CustomUserDetailsService.java
   │  │  │  └─ SecurityUserContext.java
   │  │  ├─ controller/
   │  │  │  ├─ AuthController.java
   │  │  │  ├─ UserController.java
   │  │  │  ├─ CategoryController.java
   │  │  │  ├─ ItemController.java
   │  │  │  ├─ UploadController.java
   │  │  │  ├─ ExchangeRequestController.java
   │  │  │  ├─ ExchangeRecordController.java
   │  │  │  └─ admin/
   │  │  │     ├─ AdminDashboardController.java
   │  │  │     ├─ AdminUserController.java
   │  │  │     ├─ AdminCategoryController.java
   │  │  │     ├─ AdminItemController.java
   │  │  │     └─ AdminExchangeRecordController.java
   │  │  ├─ service/
   │  │  │  ├─ AuthService.java
   │  │  │  ├─ UserService.java
   │  │  │  ├─ CategoryService.java
   │  │  │  ├─ ItemService.java
   │  │  │  ├─ FileService.java
   │  │  │  ├─ ExchangeRequestService.java
   │  │  │  ├─ ExchangeRecordService.java
   │  │  │  └─ impl/
   │  │  │     ├─ AuthServiceImpl.java
   │  │  │     ├─ UserServiceImpl.java
   │  │  │     ├─ CategoryServiceImpl.java
   │  │  │     ├─ ItemServiceImpl.java
   │  │  │     ├─ FileServiceImpl.java
   │  │  │     ├─ ExchangeRequestServiceImpl.java
   │  │  │     └─ ExchangeRecordServiceImpl.java
   │  │  ├─ repository/
   │  │  │  ├─ SysUserRepository.java
   │  │  │  ├─ SysRoleRepository.java
   │  │  │  ├─ SysUserRoleRepository.java
   │  │  │  ├─ ItemCategoryRepository.java
   │  │  │  ├─ IdleItemRepository.java
   │  │  │  ├─ ItemImageRepository.java
   │  │  │  ├─ ExchangeRequestRepository.java
   │  │  │  └─ ExchangeRecordRepository.java
   │  │  ├─ entity/
   │  │  │  ├─ BaseEntity.java
   │  │  │  ├─ SysUser.java
   │  │  │  ├─ SysRole.java
   │  │  │  ├─ SysUserRole.java
   │  │  │  ├─ ItemCategory.java
   │  │  │  ├─ IdleItem.java
   │  │  │  ├─ ItemImage.java
   │  │  │  ├─ ExchangeRequest.java
   │  │  │  └─ ExchangeRecord.java
   │  │  ├─ dto/
   │  │  │  ├─ auth/
   │  │  │  │  ├─ LoginDTO.java
   │  │  │  │  ├─ RegisterDTO.java
   │  │  │  │  └─ ChangePasswordDTO.java
   │  │  │  ├─ user/
   │  │  │  │  ├─ UserUpdateProfileDTO.java
   │  │  │  │  └─ UserStatusUpdateDTO.java
   │  │  │  ├─ category/
   │  │  │  │  ├─ CategoryCreateDTO.java
   │  │  │  │  └─ CategoryUpdateDTO.java
   │  │  │  ├─ item/
   │  │  │  │  ├─ ItemCreateDTO.java
   │  │  │  │  ├─ ItemUpdateDTO.java
   │  │  │  │  ├─ ItemQueryDTO.java
   │  │  │  │  └─ ItemStatusUpdateDTO.java
   │  │  │  └─ exchange/
   │  │  │     ├─ ExchangeRequestCreateDTO.java
   │  │  │     ├─ ExchangeRequestHandleDTO.java
   │  │  │     └─ ExchangeRequestCancelDTO.java
   │  │  ├─ vo/
   │  │  │  ├─ auth/
   │  │  │  │  ├─ LoginVO.java
   │  │  │  │  └─ CurrentUserVO.java
   │  │  │  ├─ user/
   │  │  │  │  └─ UserVO.java
   │  │  │  ├─ category/
   │  │  │  │  └─ CategoryVO.java
   │  │  │  ├─ item/
   │  │  │  │  ├─ ItemSimpleVO.java
   │  │  │  │  ├─ ItemDetailVO.java
   │  │  │  │  └─ ItemImageVO.java
   │  │  │  └─ exchange/
   │  │  │     ├─ ExchangeRequestVO.java
   │  │  │     └─ ExchangeRecordVO.java
   │  │  ├─ enums/
   │  │  │  ├─ UserStatusEnum.java
   │  │  │  ├─ ItemStatusEnum.java
   │  │  │  ├─ ExchangeRequestStatusEnum.java
   │  │  │  ├─ ExchangeRecordStatusEnum.java
   │  │  │  └─ RoleCodeEnum.java
   │  │  ├─ exception/
   │  │  │  ├─ BizException.java
   │  │  │  ├─ GlobalExceptionHandler.java
   │  │  │  └─ UnauthorizedException.java
   │  │  ├─ util/
   │  │  │  ├─ JwtUtil.java
   │  │  │  ├─ PasswordUtil.java
   │  │  │  ├─ BeanCopyUtil.java
   │  │  │  └─ FileUtil.java
   │  │  └─ constant/
   │  │     ├─ SecurityConstants.java
   │  │     ├─ ApiConstants.java
   │  │     └─ FileConstants.java
   │  └─ resources/
   │     ├─ application.yml
   │     ├─ application-dev.yml
   │     ├─ application-prod.yml
   │     └─ logback-spring.xml
   └─ test/
      └─ java/com/campus/idle/
         ├─ AuthControllerTest.java
         ├─ ItemControllerTest.java
         └─ ExchangeRequestServiceTest.java
```

### 1.1 选型说明（JPA vs MyBatis-Plus）

本项目优先选择 **Spring Data JPA**：
- 对毕业设计阶段更利于快速交付和后期扩展；
- 结合实体关系（用户-角色、物品-图片、申请-记录）建模直观；
- 复杂查询可通过 `Specification` / `@Query` 补充，代码结构清晰。

---

## 2. 数据库设计（MySQL 8）

> 数据库名建议：`campus_idle`

### 2.1 表关系概览

- `sys_user` 与 `sys_role`：多对多（中间表 `sys_user_role`）
- `item_category` 与 `idle_item`：一对多
- `idle_item` 与 `item_image`：一对多
- `idle_item` 与 `exchange_request`：一对多
- `sys_user` 与 `exchange_request`：
  - `from_user_id` 发起者
  - `to_user_id` 物品拥有者
- `exchange_request` 与 `exchange_record`：一对一（同意后生成）

---

### 2.2 建表 SQL（含字段注释）

> 文件落地：`sql/schema.sql`

```sql
CREATE DATABASE IF NOT EXISTS campus_idle DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE campus_idle;

-- 1) 系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名（唯一）',
    password VARCHAR(255) NOT NULL COMMENT '加密密码（BCrypt）',
    nickname VARCHAR(50) NOT NULL COMMENT '昵称',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    student_no VARCHAR(32) DEFAULT NULL COMMENT '学号/工号',
    gender TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    avatar_url VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1正常',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_phone (phone),
    INDEX idx_user_email (email),
    INDEX idx_user_status (status)
) ENGINE=InnoDB COMMENT='系统用户表';

-- 2) 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码：ROLE_USER/ROLE_ADMIN',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB COMMENT='系统角色表';

-- 3) 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_ur_role_id (role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB COMMENT='用户角色关联表';

-- 4) 物品分类表
CREATE TABLE IF NOT EXISTS item_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序号（升序）',
    icon VARCHAR(255) DEFAULT NULL COMMENT '分类图标URL',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_status (status),
    INDEX idx_category_sort (sort_no)
) ENGINE=InnoDB COMMENT='物品分类表';

-- 5) 闲置物品表
CREATE TABLE IF NOT EXISTS idle_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物品ID',
    user_id BIGINT NOT NULL COMMENT '发布人用户ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    title VARCHAR(100) NOT NULL COMMENT '物品标题',
    description TEXT COMMENT '物品描述',
    condition_level TINYINT NOT NULL DEFAULT 3 COMMENT '新旧程度：1-5',
    expected_item_desc VARCHAR(255) DEFAULT NULL COMMENT '期望交换物品描述',
    campus_location VARCHAR(100) DEFAULT NULL COMMENT '校内交易地点',
    contact_info VARCHAR(100) DEFAULT NULL COMMENT '联系方式补充',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1上架 2下架 3已交换',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    created_by BIGINT DEFAULT NULL COMMENT '创建人ID',
    updated_by BIGINT DEFAULT NULL COMMENT '更新人ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_item_user (user_id),
    INDEX idx_item_category (category_id),
    INDEX idx_item_status (status),
    INDEX idx_item_created_at (created_at),
    FULLTEXT INDEX idx_item_title_desc (title, description),
    CONSTRAINT fk_item_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES item_category(id)
) ENGINE=InnoDB COMMENT='闲置物品表';

-- 6) 物品图片表
CREATE TABLE IF NOT EXISTS item_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片访问URL',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序号（升序）',
    is_cover TINYINT NOT NULL DEFAULT 0 COMMENT '是否封面：0否 1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_image_item (item_id),
    CONSTRAINT fk_image_item FOREIGN KEY (item_id) REFERENCES idle_item(id)
) ENGINE=InnoDB COMMENT='物品图片表';

-- 7) 交换申请表
CREATE TABLE IF NOT EXISTS exchange_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交换申请ID',
    item_id BIGINT NOT NULL COMMENT '目标物品ID',
    from_user_id BIGINT NOT NULL COMMENT '申请人用户ID',
    to_user_id BIGINT NOT NULL COMMENT '物品拥有者用户ID',
    message VARCHAR(255) DEFAULT NULL COMMENT '申请留言',
    offered_item_desc VARCHAR(255) DEFAULT NULL COMMENT '申请人提供的交换物描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1待处理 2已同意 3已拒绝 4已取消',
    handled_time DATETIME DEFAULT NULL COMMENT '处理时间',
    cancelled_time DATETIME DEFAULT NULL COMMENT '取消时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_req_item (item_id),
    INDEX idx_req_from_user (from_user_id),
    INDEX idx_req_to_user (to_user_id),
    INDEX idx_req_status (status),
    CONSTRAINT fk_req_item FOREIGN KEY (item_id) REFERENCES idle_item(id),
    CONSTRAINT fk_req_from_user FOREIGN KEY (from_user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_req_to_user FOREIGN KEY (to_user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB COMMENT='交换申请表';

-- 8) 交换记录表
CREATE TABLE IF NOT EXISTS exchange_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交换记录ID',
    request_id BIGINT NOT NULL UNIQUE COMMENT '交换申请ID（唯一）',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    owner_user_id BIGINT NOT NULL COMMENT '物品原拥有者ID',
    requester_user_id BIGINT NOT NULL COMMENT '申请人ID',
    exchange_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交换达成时间',
    exchange_location VARCHAR(100) DEFAULT NULL COMMENT '约定交换地点',
    note VARCHAR(255) DEFAULT NULL COMMENT '备注',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '记录状态：1有效 0作废',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_record_item (item_id),
    INDEX idx_record_owner (owner_user_id),
    INDEX idx_record_requester (requester_user_id),
    CONSTRAINT fk_record_request FOREIGN KEY (request_id) REFERENCES exchange_request(id),
    CONSTRAINT fk_record_item FOREIGN KEY (item_id) REFERENCES idle_item(id),
    CONSTRAINT fk_record_owner FOREIGN KEY (owner_user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_record_requester FOREIGN KEY (requester_user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB COMMENT='交换记录表';
```

---

## 3. 实体类、DTO、VO 设计

> 字段与数据库保持一致；DTO 负责入参校验，VO 负责出参展示。

### 3.1 Entity 设计（核心字段）

1) `SysUser`
- `id, username, password, nickname, realName, studentNo, gender, phone, email, avatarUrl`
- `status, lastLoginTime, lastLoginIp, deleted`
- 审计字段：`createdBy, updatedBy, createdAt, updatedAt`
- 关联：`Set<SysRole> roles`

2) `SysRole`
- `id, roleCode, roleName, description, status, deleted, createdAt, updatedAt`

3) `SysUserRole`
- `id, userId, roleId, createdAt`

4) `ItemCategory`
- `id, categoryName, sortNo, icon, status, deleted, createdBy, updatedBy, createdAt, updatedAt`

5) `IdleItem`
- `id, userId, categoryId, title, description, conditionLevel, expectedItemDesc`
- `campusLocation, contactInfo, status, viewCount, deleted`
- `createdBy, updatedBy, createdAt, updatedAt`
- 关联：`List<ItemImage> images`

6) `ItemImage`
- `id, itemId, imageUrl, sortNo, isCover, createdAt`

7) `ExchangeRequest`
- `id, itemId, fromUserId, toUserId, message, offeredItemDesc`
- `status, handledTime, cancelledTime, deleted, createdAt, updatedAt`

8) `ExchangeRecord`
- `id, requestId, itemId, ownerUserId, requesterUserId`
- `exchangeTime, exchangeLocation, note, status, createdAt, updatedAt`

---

### 3.2 DTO 设计（含校验建议）

#### 认证模块
- `RegisterDTO`
  - `username` `@NotBlank @Size(min=4,max=20)`
  - `password` `@NotBlank @Pattern(...)`
  - `nickname` `@NotBlank @Size(max=50)`
  - `phone` `@Pattern(...)`
  - `email` `@Email`
- `LoginDTO`
  - `username` `@NotBlank`
  - `password` `@NotBlank`
- `ChangePasswordDTO`
  - `oldPassword` `@NotBlank`
  - `newPassword` `@NotBlank @Pattern(...)`

#### 用户模块
- `UserUpdateProfileDTO`
  - `nickname @NotBlank`
  - `realName, gender, phone, email, avatarUrl`
- `UserStatusUpdateDTO`（管理员）
  - `userId @NotNull`
  - `status @NotNull`（0/1）

#### 分类模块
- `CategoryCreateDTO`
  - `categoryName @NotBlank`
  - `sortNo @NotNull`
  - `icon, status`
- `CategoryUpdateDTO`
  - `id @NotNull`
  - `categoryName @NotBlank`
  - `sortNo @NotNull`
  - `icon, status`

#### 物品模块
- `ItemCreateDTO`
  - `categoryId @NotNull`
  - `title @NotBlank @Size(max=100)`
  - `description @NotBlank`
  - `conditionLevel @Min(1) @Max(5)`
  - `expectedItemDesc, campusLocation, contactInfo`
  - `imageUrls @NotEmpty`（最多9张）
- `ItemUpdateDTO`
  - `id @NotNull`
  - 其余字段同创建（可部分更新）
- `ItemQueryDTO`
  - `keyword, categoryId, status, pageNum, pageSize`
- `ItemStatusUpdateDTO`
  - `id @NotNull`
  - `status @NotNull`（1上架2下架3已交换）

#### 交换申请模块
- `ExchangeRequestCreateDTO`
  - `itemId @NotNull`
  - `message @Size(max=255)`
  - `offeredItemDesc @Size(max=255)`
- `ExchangeRequestHandleDTO`
  - `requestId @NotNull`
  - `action @NotBlank`（AGREE/REJECT）
  - `note`
- `ExchangeRequestCancelDTO`
  - `requestId @NotNull`

---

### 3.3 VO 设计

- `LoginVO`：`token, tokenType, expiresIn, userInfo(CurrentUserVO)`
- `CurrentUserVO`：`id, username, nickname, avatarUrl, roles, status`
- `UserVO`：用户管理列表/详情展示字段（不含密码）
- `CategoryVO`：`id, categoryName, sortNo, icon, status`
- `ItemSimpleVO`：列表字段（含封面图、分类名、发布者昵称）
- `ItemDetailVO`：详情字段（含图片列表、发布者信息、状态）
- `ItemImageVO`：`id, imageUrl, sortNo, isCover`
- `ExchangeRequestVO`：申请详情（物品标题、双方昵称、状态、时间）
- `ExchangeRecordVO`：交换记录（申请ID、物品、双方、达成时间、地点）

---

## 4. REST API 设计清单

> 前缀建议：`/api`；遵循 RESTful + 语义化子路径。

### 4.1 认证与用户

- `POST /api/auth/register` 用户注册（公开）
- `POST /api/auth/login` 用户登录（公开）
- `POST /api/auth/logout` 用户退出（登录）
- `GET /api/auth/me` 获取当前登录用户（登录）
- `PUT /api/users/profile` 修改个人资料（登录）
- `PUT /api/users/password` 修改密码（登录）

### 4.2 分类

- `GET /api/categories/options` 分类下拉（公开）
- `GET /api/categories` 分类列表（公开）
- `POST /api/categories` 新增分类（管理员）
- `PUT /api/categories/{id}` 修改分类（管理员）
- `DELETE /api/categories/{id}` 删除分类（管理员）

### 4.3 闲置物品

- `POST /api/items` 发布物品（登录）
- `PUT /api/items/{id}` 编辑我的物品（登录，资源归属校验）
- `DELETE /api/items/{id}` 删除我的物品（登录，资源归属校验）
- `GET /api/items/my` 我的物品列表（登录）
- `GET /api/items` 物品分页列表（公开）
- `GET /api/items/{id}` 物品详情（公开）
- `PUT /api/items/{id}/status` 物品状态变更（登录：本人；管理员可强制下架）

### 4.4 上传

- `POST /api/files/upload` 上传图片（登录）

### 4.5 交换申请

- `POST /api/exchange-requests` 发起交换申请（登录）
- `GET /api/exchange-requests/sent` 我发起的申请（登录）
- `GET /api/exchange-requests/received` 发给我的申请（登录）
- `PUT /api/exchange-requests/{id}/handle` 同意/拒绝（登录，必须是物品拥有者）
- `PUT /api/exchange-requests/{id}/cancel` 取消申请（登录，必须是申请人）

### 4.6 交换记录

- `GET /api/exchange-records/my` 我的交换记录（登录）
- `GET /api/admin/exchange-records` 全部交换记录（管理员）

### 4.7 后台管理

- `GET /api/admin/dashboard/statistics` 后台统计（管理员）
- `GET /api/admin/users` 用户分页（管理员）
- `PUT /api/admin/users/{id}/status` 禁用/启用用户（管理员）
- `GET /api/admin/items` 物品分页（管理员）
- `PUT /api/admin/items/{id}/force-offline` 强制下架（管理员）
- `DELETE /api/admin/items/{id}` 删除违规物品（管理员）
- `POST /api/admin/categories` 分类新增（管理员）
- `PUT /api/admin/categories/{id}` 分类更新（管理员）
- `DELETE /api/admin/categories/{id}` 分类删除（管理员）

---

## 5. 统一返回体与异常处理设计

### 5.1 统一返回体 `Result<T>`

标准结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1730000000000
}
```

字段说明：
- `code`：业务状态码（200成功，4xx/5xx错误）
- `message`：提示信息
- `data`：业务数据
- `timestamp`：服务端毫秒时间戳

分页结构 `PageResult<T>`：
- `records, total, pageNum, pageSize, totalPages`

### 5.2 全局异常处理 `GlobalExceptionHandler`

统一处理：
- `MethodArgumentNotValidException`：参数校验错误（返回第一个错误字段与消息）
- `ConstraintViolationException`：路径参数/查询参数校验失败
- `HttpMessageNotReadableException`：JSON 解析错误
- `BizException`：业务异常（如“物品已下架不可申请”）
- `AccessDeniedException`：无权限
- `AuthenticationException`：未登录或 token 无效
- `Exception`：兜底系统异常

错误码建议：
- `40001` 参数错误
- `40100` 未认证
- `40300` 无权限
- `40400` 资源不存在
- `40900` 状态冲突
- `50000` 系统异常

---

## 6. 认证与鉴权流程设计（JWT + Security）

### 6.1 登录流程

1. 用户提交用户名/密码到 `/api/auth/login`
2. 后端校验账号状态、密码（BCrypt）
3. 查询用户角色集合（`ROLE_USER` / `ROLE_ADMIN`）
4. 签发 JWT（包含 `uid、username、roles、exp`）
5. 返回 `LoginVO`（含 token 与用户信息）

### 6.2 请求鉴权流程

1. 前端在 `Authorization: Bearer <token>` 携带 JWT
2. `JwtAuthenticationFilter` 拦截并解析 token
3. 校验签名、过期时间、黑名单（可选）
4. 将认证信息写入 `SecurityContext`
5. 进入 Controller，按 `@PreAuthorize` 执行权限控制

### 6.3 退出登录

- 由于 JWT 无状态，基础方案由前端清除 token；
- 可扩展 Redis 黑名单做强制失效。

### 6.4 密码安全

- 密码只存 BCrypt 哈希，不保存明文；
- 修改密码需校验旧密码；
- 可扩展密码强度策略与登录失败锁定。

---

## 7. 接口权限矩阵（登录/管理员）

### 7.1 公开接口（无需登录）

- 注册、登录
- 分类列表/分类下拉
- 物品分页列表、物品详情

### 7.2 需登录（普通用户或管理员）

- 退出登录、获取当前用户、修改个人资料、修改密码
- 发布/编辑/删除自己的物品
- 我的物品、我的申请、收到的申请
- 发起申请、取消申请、处理申请（需资源归属校验）
- 上传文件
- 我的交换记录

### 7.3 管理员权限（`ROLE_ADMIN`）

- 后台统计
- 用户管理（禁用/启用）
- 物品管理（强制下架/删除）
- 分类管理（增删改）
- 查看全部交换记录

> 资源归属规则：
> - 编辑/删除物品：仅发布者本人或管理员
> - 处理申请：仅目标物品拥有者或管理员
> - 取消申请：仅申请发起者

---

## 8. 初始化 SQL 设计（说明）

> 文件建议：`sql/data_init.sql`

- 初始化角色：
  - `ROLE_ADMIN`（管理员）
  - `ROLE_USER`（普通用户）
- 初始化管理员账号：`admin / Admin@123456`（入库为 BCrypt）
- 初始化普通用户测试账号：`user01 / User@123456`
- 初始化常用分类：教材书籍、数码设备、运动器材、生活用品、票券卡券

---

## 9. 开发说明（第一阶段）

- 本阶段已固定后端领域模型、接口边界与权限模型；
- 第二阶段可直接按本文档生成完整 Spring Boot 代码实现；
- 与前端联调时保证：
  - 状态枚举值统一（用户/物品/申请）；
  - 分页参数统一（`pageNum/pageSize`）；
  - 统一响应结构统一（`Result<T>`）。
