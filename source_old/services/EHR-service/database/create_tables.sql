-- Create database if not exists
CREATE DATABASE IF NOT EXISTS ehrdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

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

-- Insert sample data (optional)
-- Sample Medical Visit
INSERT INTO medical_visits (patient_id, doctor_id, visit_date, hospital, department, diagnosis) 
VALUES 
(8, 'doc001', '2024-01-15 10:00:00', 'Bệnh viện Đa khoa TDTU', 'Khoa Tim mạch', 'Cao huyết áp, cần theo dõi định kỳ'),
(8, 'doc002', '2024-02-20 14:30:00', 'Bệnh viện Đa khoa TDTU', 'Khoa Nội tổng quát', 'Cảm cúm, sốt nhẹ');

-- Sample Prescriptions
INSERT INTO prescriptions (visit_id, patient_id, medication_name, dosage, frequency, quantity, instructions) 
VALUES 
(1, 8, 'Amlodipine 5mg', '1 viên', '1 lần/ngày, sau ăn', 30, 'Uống sau bữa ăn sáng, theo dõi huyết áp hàng ngày'),
(1, 8, 'Aspirin 100mg', '1 viên', '1 lần/ngày', 30, 'Uống sau ăn để tránh kích ứng dạ dày'),
(2, 8, 'Paracetamol 500mg', '1-2 viên', '3 lần/ngày khi sốt', 20, 'Uống khi sốt trên 38.5 độ C, không quá 4g/ngày');


