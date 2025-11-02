Table: api_rate_limits (For rate limiting)

CREATE TABLE api_rate_limits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    endpoint_pattern VARCHAR(255) NOT NULL,
    request_count INT DEFAULT 0,
    window_start TIMESTAMP NOT NULL,
    window_duration_seconds INT DEFAULT 60,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_endpoint (user_id, endpoint_pattern),
    INDEX idx_window_start (window_start)
);

Table: audit_logs (System-wide audit trail)

CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    service_name VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id VARCHAR(100),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    request_data JSON,
    response_status INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_service_name (service_name),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
);

