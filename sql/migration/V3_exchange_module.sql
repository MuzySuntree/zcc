USE campus_idle;

-- 交换申请查询优化索引
ALTER TABLE exchange_request
    ADD INDEX idx_req_from_status_created (from_user_id, status, created_at),
    ADD INDEX idx_req_to_status_created (to_user_id, status, created_at);

-- 交换记录查询优化索引
ALTER TABLE exchange_record
    ADD INDEX idx_record_owner_time (owner_user_id, exchange_time),
    ADD INDEX idx_record_requester_time (requester_user_id, exchange_time);
