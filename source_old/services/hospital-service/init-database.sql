-- ============================================
-- MySQL Database Script for Hospital Service
-- ============================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS hospitalsdb
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Sử dụng database
USE hospitalsdb;

-- ============================================
-- Bảng hospitals (Bệnh viện)
-- ============================================
CREATE TABLE IF NOT EXISTS hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_name VARCHAR(255) NOT NULL,
    hospital_address VARCHAR(500),
    hospital_phone VARCHAR(20),
    hospital_email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_hospital_name (hospital_name),
    INDEX idx_hospital_email (hospital_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Bảng departments (Khoa/Phòng ban)
-- ============================================
CREATE TABLE IF NOT EXISTS departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    department_phone VARCHAR(20),
    department_email VARCHAR(100),
    hospital_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    INDEX idx_department_name (department_name),
    INDEX idx_hospital_id (hospital_id),
    INDEX idx_department_email (department_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tạo user và cấp quyền (nếu cần)
-- ============================================
-- CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY '123456';
-- GRANT ALL PRIVILEGES ON hospitalsdb.* TO 'admin'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================
-- Dữ liệu mẫu (Sample Data)
-- ============================================

-- Thêm dữ liệu mẫu cho bảng hospitals
INSERT INTO hospitals (hospital_name, hospital_address, hospital_phone, hospital_email) VALUES
('Bệnh viện Đa khoa Trung ương', '123 Đường ABC, Quận 1, TP.HCM', '028-1234-5678', 'info@hospital1.vn'),
('Bệnh viện Chợ Rẫy', '201B Nguyễn Chí Thanh, Quận 5, TP.HCM', '028-3855-4137', 'contact@choray.vn'),
('Bệnh viện Nhi Đồng 1', '341 Sư Vạn Hạnh, Quận 10, TP.HCM', '028-3927-1119', 'info@nhidong1.vn')
ON DUPLICATE KEY UPDATE hospital_name=hospital_name;

-- Thêm dữ liệu mẫu cho bảng departments
INSERT INTO departments (department_name, department_phone, department_email, hospital_id) VALUES
('Khoa Nội tổng quát', '028-1234-5679', 'noitongquat@hospital1.vn', 1),
('Khoa Ngoại tổng quát', '028-1234-5680', 'ngoitongquat@hospital1.vn', 1),
('Khoa Sản', '028-1234-5681', 'san@hospital1.vn', 1),
('Khoa Nhi', '028-3855-4138', 'nhi@choray.vn', 2),
('Khoa Tim mạch', '028-3855-4139', 'timmach@choray.vn', 2),
('Khoa Cấp cứu', '028-3927-1120', 'capcuu@nhidong1.vn', 3),
('Khoa Hô hấp', '028-3927-1121', 'hohap@nhidong1.vn', 3)
ON DUPLICATE KEY UPDATE department_name=department_name;

