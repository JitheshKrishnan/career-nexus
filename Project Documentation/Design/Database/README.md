Database Indexes Summary
Key Indexes Added:

Primary Keys: All tables
Foreign Keys: All relationships
Unique Constraints: Prevent duplicates (email, tokens, user-job matches)
Regular Indexes: Frequently queried columns (user_id, status, dates)
Fulltext Indexes: Search optimization (job titles, descriptions, courses)
Composite Indexes: Multi-column queries (user_id + endpoint, etc.)


Implementation Notes
Phase 1 (Development):

# Single MySQL instance with separate databases
mysql:
  databases:
    - user_service_db
    - resume_service_db
    - job_matcher_db
    - learning_service_db
    - notification_db

Phase 2 (Production):

Separate MySQL instance per service
Consider PostgreSQL for better JSON support
Add read replicas for heavy read operations
Implement database connection pooling

# Use Flyway or Liquibase for version control
/db/migration/
  ├── user-service/
  │   ├── V1__create_users_table.sql
  │   └── V2__create_tokens_tables.sql
  ├── resume-service/
  │   └── V1__create_resume_tables.sql
  └── ...

Data Retention:

JWT blacklist: Auto-delete expired tokens (cron job)
Verification tokens: Delete after 30 days
Audit logs: Archive after 90 days
Job listings: Deactivate after expiry date

Sample Relationships

User (user_service)
  ↓
  → Resumes (resume_service) [via user_id]
  → Job Matches (job_matcher) [via user_id]
  → Learning Paths (learning_service) [via user_id]
  → Notifications (notification_service) [via user_id]

Resume
  ↓
  → Personal Info (1:1)
  → Skills (1:N)
  → Experience (1:N)
  → Education (1:N)
  → Certifications (1:N)
  → Projects (1:N)
  → Analysis (1:1)

Job
  ↓
  → Job Skills (1:N)
  → Job Matches (1:N)
  → Saved Jobs (1:N)
  → Applications (1:N)

Learning Path
  ↓
  → Milestones/Phases (1:N)
  → Milestone → Courses (M:N)
  → Learning Progress (1:1 per user)

SQL Scripts for Quick Setup
Create All Databases

-- Run this first to create all databases
CREATE DATABASE IF NOT EXISTS user_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS resume_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS job_matcher_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS learning_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS notification_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Optional: Create database users for each service
CREATE USER IF NOT EXISTS 'user_service'@'%' IDENTIFIED BY 'user_pass_123';
GRANT ALL PRIVILEGES ON user_service_db.* TO 'user_service'@'%';

CREATE USER IF NOT EXISTS 'resume_service'@'%' IDENTIFIED BY 'resume_pass_123';
GRANT ALL PRIVILEGES ON resume_service_db.* TO 'resume_service'@'%';

CREATE USER IF NOT EXISTS 'job_matcher_service'@'%' IDENTIFIED BY 'job_pass_123';
GRANT ALL PRIVILEGES ON job_matcher_db.* TO 'job_matcher_service'@'%';

CREATE USER IF NOT EXISTS 'learning_service'@'%' IDENTIFIED BY 'learning_pass_123';
GRANT ALL PRIVILEGES ON learning_service_db.* TO 'learning_service'@'%';

CREATE USER IF NOT EXISTS 'notification_service'@'%' IDENTIFIED BY 'notif_pass_123';
GRANT ALL PRIVILEGES ON notification_db.* TO 'notification_service'@'%';

FLUSH PRIVILEGES;

application.yml Configuration Examples
User Service

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_service_db?useSSL=false&serverTimezone=UTC
    username: user_service
    password: user_pass_123
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate  # Use 'update' for dev, 'validate' for prod
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migration/user-service
    baseline-on-migrate: true

Resume Service

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/resume_service_db?useSSL=false&serverTimezone=UTC
    username: resume_service
    password: resume_pass_123
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migration/resume-service

Job Matcher Service

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/job_matcher_db?useSSL=false&serverTimezone=UTC
    username: job_matcher_service
    password: job_pass_123
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migration/job-matcher-service

Entity Relationship Diagrams (ERD) - Text Format
User Service ERD

┌─────────────────┐     1:N      ┌────────────────────────┐
│     users       │──────────────│ email_verification_    │
│                 │              │      tokens            │
│ * id (PK)       │              │ * id (PK)              │
│ * email         │              │ * user_id (FK)         │
│ * password_hash │              │ * token                │
│ * full_name     │              │ * expires_at           │
│ * phone_number  │              └────────────────────────┘
│ * account_status│
└─────────────────┘
        │
        │ 1:N
        │
┌─────────────────────┐
│  refresh_tokens     │
│ * id (PK)           │
│ * user_id (FK)      │
│ * token             │
│ * expires_at        │
└─────────────────────┘

Resume Service ERD

┌──────────────────┐     1:1      ┌─────────────────────┐
│    resumes       │──────────────│ resume_personal_info│
│ * id (PK)        │              │ * id (PK)           │
│ * user_id        │              │ * resume_id (FK)    │
│ * file_name      │              │ * full_name         │
│ * file_url       │              │ * email             │
│ * parse_status   │              └─────────────────────┘
└──────────────────┘
        │
        │ 1:N
        ├────────────────┬──────────────────┬──────────────────┐
        │                │                  │                  │
┌───────────────┐ ┌──────────────┐ ┌─────────────────┐ ┌────────────────┐
│ resume_skills │ │   resume_    │ │     resume_     │ │    resume_     │
│               │ │  experience  │ │   education     │ │ certifications │
│ * id (PK)     │ │              │ │                 │ │                │
│ * resume_id   │ │ * id (PK)    │ │ * id (PK)       │ │ * id (PK)      │
│ * skill_name  │ │ * resume_id  │ │ * resume_id     │ │ * resume_id    │
│ * proficiency │ │ * company    │ │ * institution   │ │ * cert_name    │
└───────────────┘ └──────────────┘ └─────────────────┘ └────────────────┘

Job Matcher Service ERD

┌────────────────┐     1:N      ┌──────────────┐
│     jobs       │──────────────│  job_skills  │
│ * id (PK)      │              │ * id (PK)    │
│ * external_id  │              │ * job_id (FK)│
│ * title        │              │ * skill_name │
│ * company_name │              │ * is_required│
│ * location     │              └──────────────┘
│ * description  │
└────────────────┘
        │
        │ 1:N
        │
┌──────────────────┐
│   job_matches    │
│ * id (PK)        │
│ * user_id        │
│ * job_id (FK)    │
│ * match_score    │
│ * matched_skills │
│ * missing_skills │
└──────────────────┘

Learning Service ERD

┌──────────────────┐     1:N      ┌────────────────────────┐
│ learning_paths   │──────────────│ learning_path_         │
│ * id (PK)        │              │    milestones          │
│ * user_id        │              │ * id (PK)              │
│ * target_role    │              │ * path_id (FK)         │
│ * title          │              │ * phase_number         │
│ * total_phases   │              │ * phase_name           │
│ * status         │              │ * primary_skill        │
└──────────────────┘              └────────────────────────┘
        │                                   │
        │ 1:1                               │ M:N
        │                                   │
┌──────────────────┐              ┌────────────────────────┐
│ learning_progress│              │ learning_path_courses  │
│ * id (PK)        │              │ * id (PK)              │
│ * user_id        │              │ * milestone_id (FK)    │
│ * path_id (FK)   │              │ * course_id (FK)       │
│ * current_phase  │              │ * order_index          │
│ * completed_     │              └────────────────────────┘
│   courses (JSON) │                        │
└──────────────────┘                        │
                                            │
                                   ┌────────────────┐
                                   │    courses     │
                                   │ * id (PK)      │
                                   │ * external_id  │
                                   │ * title        │
                                   │ * source       │
                                   │ * duration_hrs │
                                   └────────────────┘

Common Query Patterns & Indexes
1. User Login (User Service)

-- Query
SELECT id, email, password_hash, account_status 
FROM users 
WHERE email = 'john@example.com' AND account_status = 'ACTIVE';

-- Optimized with: INDEX idx_email (email)

2. Get User's Resume with Skills (Resume Service)

-- Query
SELECT r.*, rs.skill_name, rs.proficiency_level
FROM resumes r
LEFT JOIN resume_skills rs ON r.id = rs.resume_id
WHERE r.user_id = 123 AND r.parse_status = 'COMPLETED'
ORDER BY r.uploaded_at DESC
LIMIT 1;

-- Optimized with: 
-- INDEX idx_user_id (user_id)
-- INDEX idx_parse_status (parse_status)

3. Find Matching Jobs (Job Matcher Service)

-- Complex query with FULLTEXT search
SELECT j.*, jm.match_score
FROM jobs j
LEFT JOIN job_matches jm ON j.id = jm.job_id AND jm.user_id = 123
WHERE j.is_active = TRUE
  AND MATCH(j.title, j.description) AGAINST ('DevOps Engineer' IN NATURAL LANGUAGE MODE)
  AND j.location LIKE '%Bangalore%'
ORDER BY jm.match_score DESC, j.posted_date DESC
LIMIT 20;

-- Optimized with:
-- FULLTEXT idx_title_description (title, description)
-- INDEX idx_is_active (is_active)
-- INDEX idx_location (location)

4. User's Learning Progress (Learning Service)

sql-- Get complete learning path with progress
SELECT 
    lp.id, lp.title, lp.status,
    lpm.phase_number, lpm.phase_name,
    COUNT(lpc.course_id) as total_courses,
    lprog.current_phase, lprog.completed_phases
FROM learning_paths lp
JOIN learning_path_milestones lpm ON lp.id = lpm.path_id
JOIN learning_path_courses lpc ON lpm.id = lpc.milestone_id
LEFT JOIN learning_progress lprog ON lp.id = lprog.path_id AND lprog.user_id = 123
WHERE lp.user_id = 123 AND lp.status = 'IN_PROGRESS'
GROUP BY lp.id, lpm.phase_number
ORDER BY lpm.phase_number;

-- Optimized with indexes on foreign keys

5. User's Unread Notifications (Notification Service)

sql-- Query
SELECT id, type, title, message, created_at
FROM notifications
WHERE user_id = 123 AND is_read = FALSE
ORDER BY created_at DESC
LIMIT 10;

-- Optimized with: 
-- INDEX idx_user_id (user_id)
-- INDEX idx_is_read (is_read)

Data Seeding Scripts (For Development/Testing)
Seed Skills Master Data

sql-- Insert common skills for matching
INSERT INTO skill_dependencies (skill_name, prerequisite_skill, is_required) VALUES
('Kubernetes', 'Docker', TRUE),
('Docker', 'Linux', TRUE),
('AWS', 'Cloud Fundamentals', FALSE),
('Terraform', 'AWS', TRUE),
('Jenkins', 'CI/CD Basics', FALSE),
('React', 'JavaScript', TRUE),
('Spring Boot', 'Java', TRUE),
('Django', 'Python', TRUE);

Seed Sample Jobs (Job Matcher)

USE job_matcher_db;

INSERT INTO jobs (external_job_id, source, title, company_name, location, job_type, 
                  experience_required, salary_min, salary_max, description, 
                  posted_date, is_active) VALUES
('naukri_12345', 'NAUKRI', 'Senior DevOps Engineer', 'Tech Corp India', 
 'Bangalore', 'FULL_TIME', '5-8 years', 2500000, 3500000,
 'Looking for experienced DevOps engineer with AWS, Kubernetes, and Terraform experience.',
 CURDATE(), TRUE),

('linkedin_67890', 'LINKEDIN', 'Full Stack Developer', 'StartupXYZ', 
 'Remote', 'REMOTE', '3-5 years', 1800000, 2500000,
 'Full stack role with React, Node.js, and MongoDB.',
 CURDATE(), TRUE);

-- Add skills for these jobs
INSERT INTO job_skills (job_id, skill_name, is_required, proficiency_level) VALUES
(1, 'AWS', TRUE, 'ADVANCED'),
(1, 'Kubernetes', TRUE, 'ADVANCED'),
(1, 'Docker', TRUE, 'INTERMEDIATE'),
(1, 'Terraform', TRUE, 'INTERMEDIATE'),
(1, 'Jenkins', FALSE, 'INTERMEDIATE'),

(2, 'React', TRUE, 'ADVANCED'),
(2, 'Node.js', TRUE, 'ADVANCED'),
(2, 'MongoDB', TRUE, 'INTERMEDIATE'),
(2, 'JavaScript', TRUE, 'EXPERT');

Seed Sample Courses (Learning Service)

USE learning_service_db;

INSERT INTO courses (external_course_id, source, title, instructor_name, 
                     course_url, price, duration_hours, difficulty_level, 
                     rating, has_certificate, is_free) VALUES
('udemy_aws_arch', 'UDEMY', 'AWS Certified Solutions Architect - Associate', 
 'Stephane Maarek', 'https://udemy.com/aws-certified-solutions-architect', 
 499, 27, 'INTERMEDIATE', 4.7, TRUE, FALSE),

('coursera_docker', 'COURSERA', 'Docker for Absolute Beginners', 
 'Mumshad Mannambeth', 'https://coursera.org/docker-beginners', 
 0, 5, 'BEGINNER', 4.5, TRUE, TRUE),

('youtube_k8s', 'YOUTUBE', 'Kubernetes Tutorial for Beginners - Full Course', 
 'TechWorld with Nana', 'https://youtube.com/watch?v=X48VuDVv0do', 
 0, 4, 'BEGINNER', 4.8, FALSE, TRUE);

-- Add skills taught by these courses
INSERT INTO course_skills (course_id, skill_name, is_primary) VALUES
(1, 'AWS', TRUE),
(1, 'Cloud Architecture', TRUE),
(1, 'EC2', FALSE),
(1, 'S3', FALSE),

(2, 'Docker', TRUE),
(2, 'Containerization', TRUE),

(3, 'Kubernetes', TRUE),
(3, 'Container Orchestration', TRUE);

Seed Email Templates (Notification Service)

USE notification_db;

INSERT INTO email_templates (template_name, subject_template, html_template, 
                             text_template, variables, is_active) VALUES
('job_match_alert', 
 '🎯 {{matchScore}}% Match: {{jobTitle}} at {{company}}',
 '<html><body><h1>Hi {{userName}},</h1><p>We found a great job match for you!</p><h2>{{jobTitle}}</h2><p>Company: {{company}}</p><p>Match Score: {{matchScore}}%</p><a href="{{jobUrl}}">View Job</a></body></html>',
 'Hi {{userName}}, We found a {{matchScore}}% match: {{jobTitle}} at {{company}}. View at {{jobUrl}}',
 '["userName", "jobTitle", "company", "matchScore", "jobUrl"]',
 TRUE),

('phase_completed',
 '🎉 Phase Completed: {{phaseName}}',
 '<html><body><h1>Congratulations {{userName}}!</h1><p>You completed {{phaseName}}</p><p>Skill Added: {{skillAdded}}</p><p>Next Phase: {{nextPhase}}</p></body></html>',
 'Congratulations {{userName}}! You completed {{phaseName}}. Next: {{nextPhase}}',
 '["userName", "phaseName", "skillAdded", "nextPhase"]',
 TRUE),

('email_verification',
 'Verify your Career Nexus account',
 '<html><body><h1>Welcome {{userName}}!</h1><p>Click to verify: <a href="{{verificationUrl}}">Verify Email</a></p></body></html>',
 'Welcome {{userName}}! Verify your email: {{verificationUrl}}',
 '["userName", "verificationUrl"]',
 TRUE);

 Database Performance Optimization Tips
1. Connection Pooling (HikariCP - Spring Boot Default)

spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

2. Query Optimization
sql-- Use EXPLAIN to analyze queries
EXPLAIN SELECT * FROM jobs WHERE is_active = TRUE;

-- Add covering indexes for frequent queries
CREATE INDEX idx_jobs_active_posted 
ON jobs(is_active, posted_date DESC) 
WHERE is_active = TRUE;

3. Partitioning (For large tables)
sql-- Partition notifications by date
ALTER TABLE notifications
PARTITION BY RANGE (YEAR(created_at)) (
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

4. Archiving Old Data
sql-- Archive old notifications (run monthly)
INSERT INTO notifications_archive 
SELECT * FROM notifications 
WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);

DELETE FROM notifications 
WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);

Backup & Recovery Strategy
Automated Backups

#!/bin/bash
# Daily backup script

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/backups/mysql"

databases=("user_service_db" "resume_service_db" "job_matcher_db" "learning_service_db" "notification_db")

for db in "${databases[@]}"
do
    mysqldump -u root -p$MYSQL_ROOT_PASSWORD $db > $BACKUP_DIR/${db}_${TIMESTAMP}.sql
    gzip $BACKUP_DIR/${db}_${TIMESTAMP}.sql
done

# Keep only last 7 days of backups
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

Migration Roadmap
Phase 1: Development (Current)

✅ Single MySQL instance
✅ Separate databases per service
✅ Basic indexes
✅ Flyway migrations

Phase 2: Testing/Staging

🔄 Add read replicas
🔄 Implement caching (Redis)
🔄 Add monitoring (Prometheus + Grafana)
🔄 Query optimization

Phase 3: Production

🔜 Separate DB instances per service
🔜 Database clustering
🔜 Automated backups to S3
🔜 Connection pooling tuning
🔜 Consider NoSQL for logs (MongoDB/Elasticsearch)

Next Steps for Implementation

Create Migration Files: Use Flyway to version control your schema
Generate JPA Entities: Create entity classes matching these tables
Add Repositories: Spring Data JPA repositories for each entity
Test Locally: Verify all relationships and constraints
Add Sample Data: Use seeding scripts for testing

Would you like me to:

Generate JPA Entity classes for any specific service?
Create Flyway migration scripts?
Design the API endpoints based on this schema?
Create a database initialization script you can run directly?