Table: resumes

--! Changed --
CREATE TABLE resumes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size_kb INT,
    file_type VARCHAR(50),

    -- Generic storage fields
    storage_provider ENUM('S3', 'CLOUDINARY', 'LOCAL', 'OTHER') DEFAULT 'S3',
    storage_identifier VARCHAR(500),  -- e.g., s3_key or cloudinary public_id
    storage_metadata JSON,            -- optional, for flexible info (like folder, version, etc.)

    parse_status ENUM('UPLOADED', 'PARSING', 'COMPLETED', 'FAILED') DEFAULT 'UPLOADED',
    parse_error_message TEXT,
    quality_score INT DEFAULT 0,
    completeness_score INT DEFAULT 0,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parsed_at TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_user_id (user_id),
    INDEX idx_parse_status (parse_status),
    INDEX idx_uploaded_at (uploaded_at)
);

Table: resume_personal_info

CREATE TABLE resume_personal_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT UNIQUE NOT NULL,
    full_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    location VARCHAR(255),
    linkedin_url VARCHAR(500),
    github_url VARCHAR(500),
    portfolio_url VARCHAR(500),
    summary TEXT,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

Table: resume_skills

CREATE TABLE resume_skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    skill_category ENUM('TECHNICAL', 'SOFT', 'LANGUAGE', 'TOOL', 'FRAMEWORK', 'OTHER') DEFAULT 'TECHNICAL',
    proficiency_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') DEFAULT 'INTERMEDIATE',
    years_of_experience DECIMAL(3,1) DEFAULT 0,
    acquired_via ENUM('RESUME', 'LEARNING_PATH', 'MANUAL') DEFAULT 'RESUME',
    certificate_url VARCHAR(500),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
    INDEX idx_resume_id (resume_id),
    INDEX idx_skill_name (skill_name),
    INDEX idx_skill_category (skill_category),
    UNIQUE KEY unique_resume_skill (resume_id, skill_name)
);

Table: resume_experience

CREATE TABLE resume_experience (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    company_name VARCHAR(255),
    job_title VARCHAR(255),
    location VARCHAR(255),
    start_date DATE,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    technologies_used TEXT, -- JSON array of technologies
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
    INDEX idx_resume_id (resume_id),
    INDEX idx_company_name (company_name)
);

Table: resume_education

CREATE TABLE resume_education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    institution_name VARCHAR(255),
    degree VARCHAR(255),
    field_of_study VARCHAR(255),
    start_date DATE,
    end_date DATE,
    grade VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
    INDEX idx_resume_id (resume_id)
);

--! Changed --
Table: resume_certifications

CREATE TABLE resume_certifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    certification_name VARCHAR(255),
    issuing_organization VARCHAR(255),
    issue_date DATE,
    expiry_date DATE,
    source ENUM('RESUME', 'PROFILE') DEFAULT 'RESUME', --!Confirm if this is needed and how to optimize
    credential_id VARCHAR(255),
    credential_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
    INDEX idx_resume_id (resume_id)
);

Table: resume_projects

CREATE TABLE resume_projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT NOT NULL,
    project_name VARCHAR(255),
    description TEXT,
    technologies_used TEXT, -- JSON array
    start_date DATE,
    end_date DATE,
    project_url VARCHAR(500),
    github_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
    INDEX idx_resume_id (resume_id)
);

--? Should we have a benchmark for comparison for the columns with "has_" ?
Table: resume_analysis (Quality & Insights)

CREATE TABLE resume_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resume_id BIGINT UNIQUE NOT NULL,
    overall_score INT DEFAULT 0,
    completeness_score INT DEFAULT 0,
    keyword_optimization_score INT DEFAULT 0,
    formatting_score INT DEFAULT 0,
    has_summary BOOLEAN DEFAULT FALSE,
    has_experience BOOLEAN DEFAULT FALSE,
    has_education BOOLEAN DEFAULT FALSE,
    has_skills BOOLEAN DEFAULT FALSE,
    total_skills_count INT DEFAULT 0,
    total_experience_years DECIMAL(3,1) DEFAULT 0,
    suggestions TEXT, -- JSON array of improvement suggestions
    analyzed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);