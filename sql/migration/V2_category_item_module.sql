USE campus_idle;

-- 分类表补充索引（若不存在会报错，请按环境执行）
ALTER TABLE item_category
    ADD INDEX idx_category_name_status (category_name, status);

-- 物品表补充索引，优化分页与筛选查询
ALTER TABLE idle_item
    ADD INDEX idx_item_status_category_created (status, category_id, created_at);

-- 图片表补充索引，优化详情查询
ALTER TABLE item_image
    ADD INDEX idx_item_cover_sort (item_id, is_cover, sort_no);
