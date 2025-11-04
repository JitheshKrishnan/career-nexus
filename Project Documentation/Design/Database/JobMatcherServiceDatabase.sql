Table: jobs

CREATE TABLE jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    external_job_id VARCHAR(255) UNIQUE NOT NULL,
    source ENUM('NAUKRI', 'LINKEDIN', 'GLASSDOOR', 'INDEED', 'MANUAL') NOT NULL,
    title VARCHAR(500) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    company_logo_url VARCHAR(500),
    location VARCHAR(255),
    job_type ENUM('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'REMOTE') DEFAULT 'FULL_TIME',
    experience_required VARCHAR(50),
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    salary_currency VARCHAR(10) DEFAULT 'INR',
    description TEXT,
    requirements TEXT,
    responsibilities TEXT,
    benefits TEXT,
    application_url VARCHAR(500),
    posted_date DATE,
    expiry_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    view_count INT DEFAULT 0,
    application_count INT DEFAULT 0,
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_external_job_id (external_job_id),
    INDEX idx_source (source),
    INDEX idx_company_name (company_name),
    INDEX idx_location (location),
    INDEX idx_job_type (job_type),
    INDEX idx_posted_date (posted_date),
    INDEX idx_is_active (is_active),
    FULLTEXT idx_title_description (title, description)
);

Table: job_skills

CREATE TABLE job_skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    proficiency_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'),
    
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    INDEX idx_job_id (job_id),
    INDEX idx_skill_name (skill_name),
    UNIQUE KEY unique_job_skill (job_id, skill_name)
);

Table: job_matches

CREATE TABLE job_matches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    match_score DECIMAL(5,2) NOT NULL,
    matched_skills_count INT DEFAULT 0,
    missing_skills_count INT DEFAULT 0,
    matched_skills TEXT, -- JSON array
    missing_skills TEXT, -- JSON array
    experience_match_score DECIMAL(5,2),
    location_match_score DECIMAL(5,2),
    viewed BOOLEAN DEFAULT FALSE,
    viewed_at TIMESTAMP NULL,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_job_id (job_id),
    INDEX idx_match_score (match_score DESC),
    INDEX idx_calculated_at (calculated_at),
    UNIQUE KEY unique_user_job_match (user_id, job_id)
);

Table: user_job_searches

CREATE TABLE user_job_searches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    search_query VARCHAR(500),
    location VARCHAR(255),
    job_type VARCHAR(50),
    experience_level VARCHAR(50),
    salary_min DECIMAL(10,2),
    filters JSON, -- JSON object
    results_count INT,
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_searched_at (searched_at),
    INDEX idx_location (location),
    INDEX idx_job_type (job_type),
    INDEX idx_salary_range (salary_min, salary_max)
);

Table: saved_jobs

CREATE TABLE saved_jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    notes TEXT,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_job_id (job_id),
    UNIQUE KEY unique_user_saved_job (user_id, job_id)
);