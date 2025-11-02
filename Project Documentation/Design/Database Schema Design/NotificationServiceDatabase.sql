Table: notifications

CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('JOB_MATCH_ALERT', 'PHASE_COMPLETED', 'SKILL_ADDED', 'RESUME_PARSED', 'PASSWORD_RESET', 'COURSE_REMINDER', 'SYSTEM') NOT NULL,
    title VARCHAR(500) NOT NULL,
    message TEXT NOT NULL,
    data JSON, -- Additional structured data
    link VARCHAR(500), -- Deep link for action
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at DESC)
);

Table: notification_delivery_logs

CREATE TABLE notification_delivery_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    notification_id BIGINT,
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    channel ENUM('EMAIL', 'PUSH', 'SMS', 'IN_APP') NOT NULL,
    provider VARCHAR(50), -- SendGrid, FCM, etc.
    provider_message_id VARCHAR(255),
    recipient_address VARCHAR(255), -- email or device token
    status ENUM('QUEUED', 'SENT', 'DELIVERED', 'FAILED', 'BOUNCED') DEFAULT 'QUEUED',
    error_message TEXT,
    sent_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    clicked BOOLEAN DEFAULT FALSE,
    clicked_at TIMESTAMP NULL,
    metadata JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (notification_id) REFERENCES notifications(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_channel (channel),
    INDEX idx_status (status),
    INDEX idx_provider_message_id (provider_message_id)
);

Table: user_notification_preferences

CREATE TABLE user_notification_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    email_enabled BOOLEAN DEFAULT TRUE,
    push_enabled BOOLEAN DEFAULT TRUE,
    sms_enabled BOOLEAN DEFAULT FALSE,
    
    -- Notification type preferences
    job_alerts_enabled BOOLEAN DEFAULT TRUE,
    learning_reminders_enabled BOOLEAN DEFAULT TRUE,
    phase_completion_alerts BOOLEAN DEFAULT TRUE,
    system_notifications_enabled BOOLEAN DEFAULT TRUE,
    marketing_emails_enabled BOOLEAN DEFAULT FALSE,
    
    -- Frequency settings
    job_alert_frequency ENUM('IMMEDIATE', 'DAILY', 'WEEKLY') DEFAULT 'IMMEDIATE',
    learning_reminder_time TIME DEFAULT '09:00:00',
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id)
);

Table: user_devices (For push notifications)

CREATE TABLE user_devices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    device_type ENUM('ANDROID', 'IOS', 'WEB') NOT NULL,
    fcm_token VARCHAR(500) UNIQUE NOT NULL,
    device_name VARCHAR(255),
    os_version VARCHAR(50),
    app_version VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_fcm_token (fcm_token),
    INDEX idx_is_active (is_active)
);

Table: email_templates

CREATE TABLE email_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(100) UNIQUE NOT NULL,
    subject_template VARCHAR(500) NOT NULL,
    html_template TEXT NOT NULL,
    text_template TEXT,
    variables JSON, -- List of required variables
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_template_name (template_name),
    INDEX idx_is_active (is_active)
);

