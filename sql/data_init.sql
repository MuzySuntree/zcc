USE campus_idle;

INSERT INTO sys_role (role_code, role_name, description, status)
VALUES
('ROLE_ADMIN', '管理员', '系统管理员', 1),
('ROLE_USER', '普通用户', '普通平台用户', 1)
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name), description=VALUES(description), status=VALUES(status);

-- BCrypt: Admin@123456 / User@123456 (示例哈希，实际可在启动时重新生成)
INSERT INTO sys_user (username, password, nickname, status)
VALUES
('admin', '$2a$10$7EqJtq98hPqEX7fNZaFWo.OH8Y9B8K/Y3FdVtOfvtTDHkJ/xZ4wVu', '系统管理员', 1),
('user01', '$2a$10$7EqJtq98hPqEX7fNZaFWo.OH8Y9B8K/Y3FdVtOfvtTDHkJ/xZ4wVu', '测试用户01', 1)
ON DUPLICATE KEY UPDATE nickname=VALUES(nickname), status=VALUES(status);

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u, sys_role r
WHERE u.username='admin' AND r.role_code='ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), role_id=VALUES(role_id);

INSERT INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id
FROM sys_user u, sys_role r
WHERE u.username='user01' AND r.role_code='ROLE_USER'
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), role_id=VALUES(role_id);

INSERT INTO item_category (category_name, sort_no, status)
VALUES
('教材书籍', 1, 1),
('数码设备', 2, 1),
('运动器材', 3, 1),
('生活用品', 4, 1),
('票券卡券', 5, 1)
ON DUPLICATE KEY UPDATE sort_no=VALUES(sort_no), status=VALUES(status);
