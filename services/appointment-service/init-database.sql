-- Appointment Service Database Initialization
-- Database: appointmentsdb

USE appointmentsdb;

-- ============================================
-- Table: appointments
-- Quản lý lịch hẹn khám bệnh
-- ============================================
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    
    -- Doctor Information
    doctor_id INT NOT NULL COMMENT 'Reference to doctor in doctor-service',
    doctor_name VARCHAR(255) COMMENT 'Denormalized for quick access',
    
    -- Patient Information
    patient_id INT NOT NULL COMMENT 'Reference to patient in patient-service',
    patient_name VARCHAR(255) COMMENT 'Denormalized for quick access',
    
    -- Hospital Information
    hospital_id INT NOT NULL COMMENT 'Reference to hospital in hospital-service',
    hospital_name VARCHAR(255) COMMENT 'Denormalized for quick access',
    
    -- Department Information
    department_id INT COMMENT 'Reference to department in hospital-service',
    department_name VARCHAR(255) COMMENT 'Denormalized for quick access',
    
    -- Time Slot Reference
    time_slot_id INT COMMENT 'Reference to time_slot in doctor-service',
    
    -- Appointment Details
    appointment_date_time TIMESTAMP NOT NULL COMMENT 'Thời gian hẹn khám',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW',
    
    -- Additional Information
    notes TEXT COMMENT 'Ghi chú của bệnh nhân',
    reason VARCHAR(500) COMMENT 'Lý do khám bệnh',
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_status 
        CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'NO_SHOW'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: appointment_history
-- Lưu lịch sử thay đổi của appointment (optional)
-- ============================================
CREATE TABLE IF NOT EXISTS appointment_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20),
    old_date_time TIMESTAMP,
    new_date_time TIMESTAMP,
    changed_by VARCHAR(100) COMMENT 'User who made the change',
    change_reason TEXT COMMENT 'Lý do thay đổi',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_appointment_id (appointment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Appointments
-- ============================================
INSERT INTO appointments (
    doctor_id, doctor_name, 
    patient_id, patient_name,
    hospital_id, hospital_name,
    department_id, department_name,
    time_slot_id, appointment_date_time,
    status, notes, reason
)
VALUES 
    -- Appointment 1: Confirmed
    (
        1, 'Dr. Nguyen Van A',
        101, 'Nguyen Thi Lan',
        1, 'Bach Mai Hospital',
        1, 'Cardiology Department',
        1, '2024-11-25 08:00:00',
        'CONFIRMED', 'Bệnh nhân đến đúng giờ', 'Khám tim mạch định kỳ'
    ),
    
    -- Appointment 2: Pending
    (
        1, 'Dr. Nguyen Van A',
        102, 'Tran Van Minh',
        1, 'Bach Mai Hospital',
        1, 'Cardiology Department',
        2, '2024-11-25 08:30:00',
        'PENDING', NULL, 'Đau ngực, khó thở'
    ),
    
    -- Appointment 3: Confirmed
    (
        2, 'Dr. Tran Thi B',
        103, 'Le Thi Hoa',
        1, 'Bach Mai Hospital',
        2, 'Neurology Department',
        41, '2024-11-25 14:00:00',
        'CONFIRMED', NULL, 'Đau đầu kéo dài'
    ),
    
    -- Appointment 4: Completed
    (
        1, 'Dr. Nguyen Van A',
        104, 'Pham Van Tuan',
        1, 'Bach Mai Hospital',
        1, 'Cardiology Department',
        NULL, '2024-11-20 09:00:00',
        'COMPLETED', 'Khám xong, kê đơn thuốc', 'Tái khám sau phẫu thuật'
    ),
    
    -- Appointment 5: Cancelled
    (
        2, 'Dr. Tran Thi B',
        105, 'Hoang Thi Mai',
        1, 'Bach Mai Hospital',
        2, 'Neurology Department',
        NULL, '2024-11-22 15:00:00',
        'CANCELLED', 'Bệnh nhân hủy do bận việc gấp', 'Khám tổng quát'
    ),
    
    -- Appointment 6: No Show
    (
        3, 'Dr. Le Van C',
        106, 'Nguyen Van Hai',
        2, 'Cho Ray Hospital',
        3, 'Pediatrics Department',
        NULL, '2024-11-21 10:00:00',
        'NO_SHOW', 'Bệnh nhân không đến và không thông báo', 'Khám nhi khoa'
    ),
    
    -- Appointment 7: Pending - Future
    (
        3, 'Dr. Le Van C',
        107, 'Tran Thi Huong',
        2, 'Cho Ray Hospital',
        3, 'Pediatrics Department',
        NULL, '2024-11-26 10:30:00',
        'PENDING', NULL, 'Tiêm chủng cho trẻ'
    ),
    
    -- Appointment 8: Confirmed - Future
    (
        4, 'Dr. Pham Thi D',
        108, 'Le Van Khanh',
        2, 'Cho Ray Hospital',
        4, 'Dermatology Department',
        NULL, '2024-11-27 09:00:00',
        'CONFIRMED', NULL, 'Khám da liễu, mụn trứng cá'
    )
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- ============================================
-- Views
-- ============================================

-- View: Upcoming Appointments
CREATE OR REPLACE VIEW upcoming_appointments AS
SELECT 
    appointment_id,
    doctor_id,
    doctor_name,
    patient_id,
    patient_name,
    hospital_id,
    hospital_name,
    appointment_date_time,
    status,
    reason,
    notes
FROM appointments
WHERE appointment_date_time >= NOW()
  AND status IN ('PENDING', 'CONFIRMED')
ORDER BY appointment_date_time ASC;

-- View: Today's Appointments
CREATE OR REPLACE VIEW today_appointments AS
SELECT 
    appointment_id,
    doctor_id,
    doctor_name,
    patient_id,
    patient_name,
    hospital_id,
    hospital_name,
    appointment_date_time,
    status,
    reason,
    notes
FROM appointments
WHERE DATE(appointment_date_time) = CURDATE()
  AND status IN ('PENDING', 'CONFIRMED')
ORDER BY appointment_date_time ASC;

-- View: Appointment Statistics by Doctor
CREATE OR REPLACE VIEW appointment_stats_by_doctor AS
SELECT 
    doctor_id,
    doctor_name,
    COUNT(*) AS total_appointments,
    SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,
    SUM(CASE WHEN status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_count,
    SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_count,
    SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_count,
    SUM(CASE WHEN status = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show_count
FROM appointments
GROUP BY doctor_id, doctor_name;

-- View: Appointment Statistics by Patient
CREATE OR REPLACE VIEW appointment_stats_by_patient AS
SELECT 
    patient_id,
    patient_name,
    COUNT(*) AS total_appointments,
    SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) AS pending_count,
    SUM(CASE WHEN status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_count,
    SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_count,
    SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_count,
    SUM(CASE WHEN status = 'NO_SHOW' THEN 1 ELSE 0 END) AS no_show_count
FROM appointments
GROUP BY patient_id, patient_name;

-- ============================================
-- Stored Procedures
-- ============================================

-- Procedure: Cancel Appointment
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS cancel_appointment(
    IN p_appointment_id INT,
    IN p_reason TEXT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    
    -- Get current status
    SELECT status INTO v_old_status
    FROM appointments
    WHERE appointment_id = p_appointment_id;
    
    -- Update appointment status
    UPDATE appointments
    SET status = 'CANCELLED',
        notes = CONCAT(COALESCE(notes, ''), '\nCancelled: ', COALESCE(p_reason, 'No reason provided'))
    WHERE appointment_id = p_appointment_id;
    
    -- Log to history
    INSERT INTO appointment_history (appointment_id, old_status, new_status, change_reason)
    VALUES (p_appointment_id, v_old_status, 'CANCELLED', p_reason);
END //
DELIMITER ;

-- Procedure: Confirm Appointment
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS confirm_appointment(
    IN p_appointment_id INT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    
    -- Get current status
    SELECT status INTO v_old_status
    FROM appointments
    WHERE appointment_id = p_appointment_id;
    
    -- Update appointment status
    UPDATE appointments
    SET status = 'CONFIRMED'
    WHERE appointment_id = p_appointment_id;
    
    -- Log to history
    INSERT INTO appointment_history (appointment_id, old_status, new_status, change_reason)
    VALUES (p_appointment_id, v_old_status, 'CONFIRMED', 'Appointment confirmed');
END //
DELIMITER ;

-- Procedure: Complete Appointment
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS complete_appointment(
    IN p_appointment_id INT,
    IN p_notes TEXT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    
    -- Get current status
    SELECT status INTO v_old_status
    FROM appointments
    WHERE appointment_id = p_appointment_id;
    
    -- Update appointment status
    UPDATE appointments
    SET status = 'COMPLETED',
        notes = COALESCE(p_notes, notes)
    WHERE appointment_id = p_appointment_id;
    
    -- Log to history
    INSERT INTO appointment_history (appointment_id, old_status, new_status, change_reason)
    VALUES (p_appointment_id, v_old_status, 'COMPLETED', 'Appointment completed');
END //
DELIMITER ;

-- ============================================
-- Triggers
-- ============================================

-- Trigger: Auto log status changes
DELIMITER //
CREATE TRIGGER IF NOT EXISTS after_appointment_status_update
AFTER UPDATE ON appointments
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO appointment_history (
            appointment_id,
            old_status,
            new_status,
            old_date_time,
            new_date_time,
            change_reason
        )
        VALUES (
            NEW.appointment_id,
            OLD.status,
            NEW.status,
            OLD.appointment_date_time,
            NEW.appointment_date_time,
            'Auto logged by trigger'
        );
    END IF;
END //
DELIMITER ;

-- ============================================
-- End of Appointment Service Database Initialization
-- ============================================

