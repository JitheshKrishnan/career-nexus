Table: courses

CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    external_course_id VARCHAR(255) UNIQUE NOT NULL,
    source ENUM('UDEMY', 'COURSERA', 'YOUTUBE', 'PLURALSIGHT', 'EDEX', 'OTHER') NOT NULL,
    title VARCHAR(500) NOT NULL,
    instructor_name VARCHAR(255),
    description TEXT,
    thumbnail_url VARCHAR(500),
    course_url VARCHAR(500) NOT NULL,
    price DECIMAL(10,2),
    currency VARCHAR(10) DEFAULT 'INR',
    duration_hours DECIMAL(5,1),
    difficulty_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'ALL_LEVELS'),
    rating DECIMAL(3,2),
    review_count INT DEFAULT 0,
    enrollment_count INT DEFAULT 0,
    language VARCHAR(50) DEFAULT 'English',
    has_certificate BOOLEAN DEFAULT FALSE,
    is_free BOOLEAN DEFAULT FALSE,
    last_updated DATE,
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_source (source),
    INDEX idx_difficulty_level (difficulty_level),
    INDEX idx_is_free (is_free),
    INDEX idx_rating (rating DESC),
    FULLTEXT idx_title_description (title, description)
);

Table: course_skills (Skills taught by course)

CREATE TABLE course_skills (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_course_id (course_id),
    INDEX idx_skill_name (skill_name),
    UNIQUE KEY unique_course_skill (course_id, skill_name)
);

Table: skill_dependencies (Prerequisite skills)

CREATE TABLE skill_dependencies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(255) NOT NULL,
    prerequisite_skill VARCHAR(255) NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    
    INDEX idx_skill_name (skill_name),
    INDEX idx_prerequisite (prerequisite_skill),
    UNIQUE KEY unique_skill_dependency (skill_name, prerequisite_skill)
);

Table: learning_paths

CREATE TABLE learning_paths (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    target_role VARCHAR(255),
    title VARCHAR(500) NOT NULL,
    description TEXT,
    total_phases INT DEFAULT 0,
    total_courses INT DEFAULT 0,
    estimated_duration_hours DECIMAL(6,1),
    difficulty_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'MIXED'),
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'ABANDONED') DEFAULT 'NOT_STARTED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

Table: learning_path_milestones (Phases in learning path)

CREATE TABLE learning_path_milestones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    path_id BIGINT NOT NULL,
    phase_number INT NOT NULL,
    phase_name VARCHAR(255) NOT NULL,
    description TEXT,
    estimated_duration_hours DECIMAL(5,1),
    primary_skill VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,
    INDEX idx_path_id (path_id),
    UNIQUE KEY unique_path_phase (path_id, phase_number)
);

Table: learning_path_courses (Courses in each phase)

CREATE TABLE learning_path_courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    milestone_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    order_index INT NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (milestone_id) REFERENCES learning_path_milestones(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_milestone_id (milestone_id),
    INDEX idx_course_id (course_id),
    UNIQUE KEY unique_milestone_course (milestone_id, course_id)
);

Table: learning_progress

CREATE TABLE learning_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    path_id BIGINT NOT NULL,
    current_phase INT DEFAULT 1,
    completed_phases INT DEFAULT 0,
    completed_courses JSON, -- Array of course IDs
    total_time_spent_hours DECIMAL(6,1) DEFAULT 0,
    last_activity_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_path_id (path_id),
    UNIQUE KEY unique_user_path (user_id, path_id)
);

Table: course_completion_history

CREATE TABLE course_completion_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    path_id BIGINT,
    course_id BIGINT NOT NULL,
    course_source ENUM('UDEMY', 'COURSERA', 'YOUTUBE', 'OTHER'),
    course_title VARCHAR(500),
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    certificate_url VARCHAR(500),
    time_spent_hours DECIMAL(5,1),
    
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_path_id (path_id),
    INDEX idx_completed_at (completed_at)
);

Table: phase_completion_history

CREATE TABLE phase_completion_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    path_id BIGINT NOT NULL,
    phase_id BIGINT NOT NULL,
    phase_number INT,
    phase_name VARCHAR(255),
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,
    FOREIGN KEY (phase_id) REFERENCES learning_path_milestones(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_path_id (path_id)
);

Table: skill_gap_analysis

CREATE TABLE skill_gap_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    target_role VARCHAR(255),
    current_skills TEXT, -- JSON array
    required_skills TEXT, -- JSON array
    missing_skills TEXT, -- JSON array
    skill_gaps_count INT DEFAULT 0,
    priority_skills TEXT, -- JSON array (ordered by importance)
    analyzed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_target_role (target_role),
    INDEX idx_analyzed_at (analyzed_at)
);