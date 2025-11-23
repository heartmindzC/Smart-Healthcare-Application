-- Doctor Service Database Initialization
-- Database: doctorsdb

USE doctorsdb;

-- ============================================
-- Table: doctors
-- ============================================
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hospital_id INT NOT NULL,
    department VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    birth DATE,
    registration_at DATE,
    gender VARCHAR(20),
    license_id VARCHAR(100) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_hospital_id (hospital_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: time_slots
-- Quản lý khung giờ làm việc của bác sĩ
-- ============================================
CREATE TABLE IF NOT EXISTS time_slots (
    time_slot_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week VARCHAR(20) COMMENT 'MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY',
    start_time TIME NOT NULL COMMENT 'Giờ bắt đầu (VD: 08:00)',
    end_time TIME NOT NULL COMMENT 'Giờ kết thúc (VD: 08:30)',
    is_available BOOLEAN DEFAULT TRUE COMMENT 'TRUE = còn trống, FALSE = đã được đặt',
    specific_date DATE COMMENT 'NULL = lặp lại hàng tuần, có giá trị = ngày cụ thể (override recurring)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    CONSTRAINT fk_timeslot_doctor 
        FOREIGN KEY (doctor_id) 
        REFERENCES doctors(doctor_id) 
        ON DELETE CASCADE,
    
    -- Indexes
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_is_available (is_available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Doctors
-- ============================================
INSERT INTO doctors (user_id, hospital_id, department, full_name, birth, registration_at, gender, license_id, is_active)
VALUES 
    (1, 1, 'Cardiology', 'Dr. Nguyen Van A', '1980-05-15', '2020-01-10', 'MALE', 'DOC001', TRUE),
    (2, 1, 'Neurology', 'Dr. Tran Thi B', '1985-08-20', '2020-02-15', 'FEMALE', 'DOC002', TRUE),
    (3, 2, 'Pediatrics', 'Dr. Le Van C', '1978-12-10', '2019-11-05', 'MALE', 'DOC003', TRUE),
    (4, 2, 'Dermatology', 'Dr. Pham Thi D', '1990-03-25', '2021-03-20', 'FEMALE', 'DOC004', TRUE),
    (5, 1, 'Orthopedics', 'Dr. Hoang Van E', '1982-07-30', '2020-06-01', 'MALE', 'DOC005', TRUE)
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);

-- ============================================
-- Sample Data - Time Slots
-- Tạo lịch làm việc mẫu cho bác sĩ
-- ============================================

-- Bác sĩ 1: Thứ 2-6, sáng 8:00-12:00, mỗi slot 30 phút
INSERT INTO time_slots (doctor_id, day_of_week, start_time, end_time, is_available, specific_date)
VALUES 
    -- Thứ 2
    (1, 'MONDAY', '08:00:00', '08:30:00', TRUE, NULL),
    (1, 'MONDAY', '08:30:00', '09:00:00', TRUE, NULL),
    (1, 'MONDAY', '09:00:00', '09:30:00', TRUE, NULL),
    (1, 'MONDAY', '09:30:00', '10:00:00', TRUE, NULL),
    (1, 'MONDAY', '10:00:00', '10:30:00', TRUE, NULL),
    (1, 'MONDAY', '10:30:00', '11:00:00', TRUE, NULL),
    (1, 'MONDAY', '11:00:00', '11:30:00', TRUE, NULL),
    (1, 'MONDAY', '11:30:00', '12:00:00', TRUE, NULL),
    
    -- Thứ 3
    (1, 'TUESDAY', '08:00:00', '08:30:00', TRUE, NULL),
    (1, 'TUESDAY', '08:30:00', '09:00:00', TRUE, NULL),
    (1, 'TUESDAY', '09:00:00', '09:30:00', TRUE, NULL),
    (1, 'TUESDAY', '09:30:00', '10:00:00', TRUE, NULL),
    (1, 'TUESDAY', '10:00:00', '10:30:00', TRUE, NULL),
    (1, 'TUESDAY', '10:30:00', '11:00:00', TRUE, NULL),
    (1, 'TUESDAY', '11:00:00', '11:30:00', TRUE, NULL),
    (1, 'TUESDAY', '11:30:00', '12:00:00', TRUE, NULL),
    
    -- Thứ 4
    (1, 'WEDNESDAY', '08:00:00', '08:30:00', TRUE, NULL),
    (1, 'WEDNESDAY', '08:30:00', '09:00:00', TRUE, NULL),
    (1, 'WEDNESDAY', '09:00:00', '09:30:00', TRUE, NULL),
    (1, 'WEDNESDAY', '09:30:00', '10:00:00', TRUE, NULL),
    (1, 'WEDNESDAY', '10:00:00', '10:30:00', TRUE, NULL),
    (1, 'WEDNESDAY', '10:30:00', '11:00:00', TRUE, NULL),
    (1, 'WEDNESDAY', '11:00:00', '11:30:00', TRUE, NULL),
    (1, 'WEDNESDAY', '11:30:00', '12:00:00', TRUE, NULL),
    
    -- Thứ 5
    (1, 'THURSDAY', '08:00:00', '08:30:00', TRUE, NULL),
    (1, 'THURSDAY', '08:30:00', '09:00:00', TRUE, NULL),
    (1, 'THURSDAY', '09:00:00', '09:30:00', TRUE, NULL),
    (1, 'THURSDAY', '09:30:00', '10:00:00', TRUE, NULL),
    (1, 'THURSDAY', '10:00:00', '10:30:00', TRUE, NULL),
    (1, 'THURSDAY', '10:30:00', '11:00:00', TRUE, NULL),
    (1, 'THURSDAY', '11:00:00', '11:30:00', TRUE, NULL),
    (1, 'THURSDAY', '11:30:00', '12:00:00', TRUE, NULL),
    
    -- Thứ 6
    (1, 'FRIDAY', '08:00:00', '08:30:00', TRUE, NULL),
    (1, 'FRIDAY', '08:30:00', '09:00:00', TRUE, NULL),
    (1, 'FRIDAY', '09:00:00', '09:30:00', TRUE, NULL),
    (1, 'FRIDAY', '09:30:00', '10:00:00', TRUE, NULL),
    (1, 'FRIDAY', '10:00:00', '10:30:00', TRUE, NULL),
    (1, 'FRIDAY', '10:30:00', '11:00:00', TRUE, NULL),
    (1, 'FRIDAY', '11:00:00', '11:30:00', TRUE, NULL),
    (1, 'FRIDAY', '11:30:00', '12:00:00', TRUE, NULL)
ON DUPLICATE KEY UPDATE is_available=VALUES(is_available);

-- Bác sĩ 2: Thứ 2,4,6, chiều 14:00-17:00
INSERT INTO time_slots (doctor_id, day_of_week, start_time, end_time, is_available, specific_date)
VALUES 
    -- Thứ 2
    (2, 'MONDAY', '14:00:00', '14:30:00', TRUE, NULL),
    (2, 'MONDAY', '14:30:00', '15:00:00', TRUE, NULL),
    (2, 'MONDAY', '15:00:00', '15:30:00', TRUE, NULL),
    (2, 'MONDAY', '15:30:00', '16:00:00', TRUE, NULL),
    (2, 'MONDAY', '16:00:00', '16:30:00', TRUE, NULL),
    (2, 'MONDAY', '16:30:00', '17:00:00', TRUE, NULL),
    
    -- Thứ 4
    (2, 'WEDNESDAY', '14:00:00', '14:30:00', TRUE, NULL),
    (2, 'WEDNESDAY', '14:30:00', '15:00:00', TRUE, NULL),
    (2, 'WEDNESDAY', '15:00:00', '15:30:00', TRUE, NULL),
    (2, 'WEDNESDAY', '15:30:00', '16:00:00', TRUE, NULL),
    (2, 'WEDNESDAY', '16:00:00', '16:30:00', TRUE, NULL),
    (2, 'WEDNESDAY', '16:30:00', '17:00:00', TRUE, NULL),
    
    -- Thứ 6
    (2, 'FRIDAY', '14:00:00', '14:30:00', TRUE, NULL),
    (2, 'FRIDAY', '14:30:00', '15:00:00', TRUE, NULL),
    (2, 'FRIDAY', '15:00:00', '15:30:00', TRUE, NULL),
    (2, 'FRIDAY', '15:30:00', '16:00:00', TRUE, NULL),
    (2, 'FRIDAY', '16:00:00', '16:30:00', TRUE, NULL),
    (2, 'FRIDAY', '16:30:00', '17:00:00', TRUE, NULL)
ON DUPLICATE KEY UPDATE is_available=VALUES(is_available);

-- ============================================
-- Views
-- ============================================

-- View: Available Time Slots
CREATE OR REPLACE VIEW available_time_slots AS
SELECT 
    ts.time_slot_id,
    ts.doctor_id,
    d.full_name AS doctor_name,
    d.department,
    ts.day_of_week,
    ts.start_time,
    ts.end_time,
    ts.specific_date,
    ts.is_available
FROM time_slots ts
INNER JOIN doctors d ON ts.doctor_id = d.doctor_id
WHERE ts.is_available = TRUE AND d.is_active = TRUE;

-- ============================================
-- End of Doctor Service Database Initialization
-- ============================================

