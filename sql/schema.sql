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
