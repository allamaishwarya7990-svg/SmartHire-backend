-- ==============================================================================
-- SmartHire Flyway Migration V1: Complete Core Relational Schema
-- Database: MySQL 8.0+
-- Encoding: UTF8MB4
-- ==============================================================================

-- 1. Ensure `users` has `status` column
-- Note: MySQL 8.0.19+ supports ALTER TABLE ... ADD COLUMN IF NOT EXISTS
ALTER TABLE users ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'ACTIVE';

-- 2. Companies Table (Recruiter Company Profile)
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL,
    industry VARCHAR(255),
    company_size VARCHAR(100),
    website VARCHAR(255),
    location VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(100),
    founded VARCHAR(50),
    description TEXT,
    is_verified BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_company_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Jobs Table (Postings)
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_id BIGINT NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    work_mode VARCHAR(100) DEFAULT 'On-site',
    experience VARCHAR(100) NOT NULL,
    education VARCHAR(255) NOT NULL,
    skills TEXT NOT NULL,
    salary VARCHAR(100),
    openings INT DEFAULT 1,
    description TEXT NOT NULL,
    responsibilities TEXT,
    requirements TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_recruiter FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_job_status (status),
    INDEX idx_job_recruiter (recruiter_id),
    INDEX idx_job_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Job Applications Table
CREATE TABLE IF NOT EXISTS job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    applicant_id BIGINT NOT NULL,
    cover_message TEXT,
    resume_path VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    eligibility_score INT DEFAULT 0,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_application_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    CONSTRAINT fk_application_applicant FOREIGN KEY (applicant_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_applicant_job (applicant_id, job_id),
    INDEX idx_app_job (job_id),
    INDEX idx_app_applicant (applicant_id),
    INDEX idx_app_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Saved Jobs Table
CREATE TABLE IF NOT EXISTS saved_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_saved_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_saved_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_job (user_id, job_id),
    INDEX idx_saved_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. User Settings Table
CREATE TABLE IF NOT EXISTS user_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    email_notifications BOOLEAN DEFAULT TRUE,
    application_updates BOOLEAN DEFAULT TRUE,
    job_alerts BOOLEAN DEFAULT TRUE,
    platform_announcements BOOLEAN DEFAULT TRUE,
    dark_mode BOOLEAN DEFAULT TRUE,
    reduced_motion BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_settings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Reports & Moderation Table
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    target_title VARCHAR(255),
    reason VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_report_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
