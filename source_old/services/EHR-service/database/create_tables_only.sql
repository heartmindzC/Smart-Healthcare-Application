-- SQL Script to create tables for EHR Service
-- Database: ehrdb
-- Port: 3310

USE ehrdb;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS prescriptions;
DROP TABLE IF EXISTS medical_visits;

-- Create medical_visits table
CREATE TABLE medical_visits (
    visit_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id VARCHAR(255) NOT NULL,
    visit_date DATETIME NOT NULL,
    hospital VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    diagnosis TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_visit_date (visit_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create prescriptions table
CREATE TABLE prescriptions (
    prescription_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    visit_id BIGINT NOT NULL,
    patient_id INT NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    frequency VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    instructions TEXT,
    prescribed_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (visit_id) REFERENCES medical_visits(visit_id) ON DELETE CASCADE,
    INDEX idx_visit_id (visit_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_prescribed_date (prescribed_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


