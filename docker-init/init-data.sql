USE usersdb;
INSERT INTO users (userId, password, phone, fullname, address, birth, email, gender, role) VALUES
('admin-001', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0900000001', 'Administrator', '123 Admin Street, HCMC', '1980-01-01', 'admin@smarthealthcare.com', 'MALE', 'ADMIN');

-- Doctor Users
INSERT INTO users (userId, password, phone, fullname, address, birth, email, gender, role) VALUES
('user-doctor-001', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0901234567', 'Dr. Nguyen Van A', '456 Doctor Lane, HCMC', '1975-05-15', 'dr.nguyenvana@smarthealthcare.com', 'MALE', 'DOCTOR'),
('user-doctor-002', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0901234568', 'Dr. Tran Thi B', '789 Medical Ave, HCMC', '1980-08-20', 'dr.tranthib@smarthealthcare.com', 'FEMALE', 'DOCTOR'),
('user-doctor-003', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0901234569', 'Dr. Le Van C', '321 Health Blvd, HCMC', '1978-03-10', 'dr.levanc@smarthealthcare.com', 'MALE', 'DOCTOR'),
('user-doctor-004', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0901234570', 'Dr. Pham Thi D', '654 Care Road, HCMC', '1985-11-25', 'dr.phamthid@smarthealthcare.com', 'FEMALE', 'DOCTOR'),
('user-doctor-005', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0901234571', 'Dr. Hoang Van E', '987 Wellness St, HCMC', '1972-07-08', 'dr.hoangvane@smarthealthcare.com', 'MALE', 'DOCTOR');

-- Patient Users
INSERT INTO users (userId, password, phone, fullname, address, birth, email, gender, role) VALUES
('user-patient-001', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345678', 'John Smith', '100 Patient Home, District 1, HCMC', '1990-06-15', 'john.smith@email.com', 'MALE', 'PATIENT'),
('user-patient-002', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345679', 'Mary Johnson', '200 Health Ave, District 3, HCMC', '1985-12-20', 'mary.johnson@email.com', 'FEMALE', 'PATIENT'),
('user-patient-003', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345680', 'Robert Wilson', '300 Medical Lane, District 5, HCMC', '1995-03-25', 'robert.wilson@email.com', 'MALE', 'PATIENT'),
('user-patient-004', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345681', 'Jennifer Brown', '400 Care Street, District 7, HCMC', '1988-09-10', 'jennifer.brown@email.com', 'FEMALE', 'PATIENT'),
('user-patient-005', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345682', 'David Lee', '500 Wellness Blvd, District 2, HCMC', '1992-01-30', 'david.lee@email.com', 'MALE', 'PATIENT'),
('user-patient-006', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345683', 'Sarah Davis', '600 Hospital Road, District 10, HCMC', '1993-07-22', 'sarah.davis@email.com', 'FEMALE', 'PATIENT'),
('user-patient-007', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345684', 'Michael Chen', '700 Clinic Ave, Binh Thanh, HCMC', '1987-04-18', 'michael.chen@email.com', 'MALE', 'PATIENT'),
('user-patient-008', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IzXlCE6F7dGfR9k3sX5qZ9Z9Z9Z9Z9Z', '0912345685', 'Emily Wang', '800 Health Center, Phu Nhuan, HCMC', '1991-11-05', 'emily.wang@email.com', 'FEMALE', 'PATIENT');

-- =====================================================
USE hospitalsdb;

-- =====================================================
-- TEST DATA: HOSPITALS TABLE
-- =====================================================
INSERT INTO hospitals (hospitalId, hospitalName, hospitalAddress, hospitalPhone, hospitalEmail) VALUES
('hospital-001', 'Smart Health Central Hospital', '01 Nguyen Hue Street, District 1, Ho Chi Minh City', '028-3822-3456', 'contact@smarthealthcentral.com'),
('hospital-002', 'Smart Health District Hospital', '200 Le Dai Hanh Street, District 11, Ho Chi Minh City', '028-3855-6789', 'info@smarthealthdistrict.com'),
('hospital-003', 'Smart Health International Clinic', '50 Dien Bien Phu Street, District 3, Ho Chi Minh City', '028-3930-1234', 'international@smarthealth.com');

-- =====================================================
-- TEST DATA: DEPARTMENTS TABLE
-- =====================================================
INSERT INTO departments (departmentId, departmentName, departmentPhone, departmentEmail, hospitalId) VALUES
-- Hospital 1 Departments
('dept-001', 'Cardiology', '028-3822-3457', 'cardiology@smarthealthcentral.com', 'hospital-001'),
('dept-002', 'Orthopedics', '028-3822-3458', 'orthopedics@smarthealthcentral.com', 'hospital-001'),
('dept-003', 'Pediatrics', '028-3822-3459', 'pediatrics@smarthealthcentral.com', 'hospital-001'),
('dept-004', 'Neurology', '028-3822-3460', 'neurology@smarthealthcentral.com', 'hospital-001'),
('dept-005', 'Dermatology', '028-3822-3461', 'dermatology@smarthealthcentral.com', 'hospital-001'),
('dept-006', 'General Medicine', '028-3822-3462', 'general@smarthealthcentral.com', 'hospital-001'),
-- Hospital 2 Departments
('dept-007', 'Internal Medicine', '028-3855-6790', 'internal@smarthealthdistrict.com', 'hospital-002'),
('dept-008', 'Surgery', '028-3855-6791', 'surgery@smarthealthdistrict.com', 'hospital-002'),
('dept-009', 'Obstetrics & Gynecology', '028-3855-6792', 'obgyn@smarthealthdistrict.com', 'hospital-002'),
-- Hospital 3 Departments
('dept-010', 'Family Medicine', '028-3930-1235', 'family@smarthealth.com', 'hospital-003'),
('dept-011', 'Dental Care', '028-3930-1236', 'dental@smarthealth.com', 'hospital-003'),
('dept-012', 'Eye Care', '028-3930-1237', 'eye@smarthealth.com', 'hospital-003');

-- =====================================================
USE doctorsdb;

-- =====================================================
-- TEST DATA: DOCTORS TABLE
-- =====================================================
INSERT INTO doctors (doctorId, userId, hospitalId, department, fullName, birth, gender, licenseId, isActive) VALUES
('doctor-001', 'user-doctor-001', 'hospital-001', 'dept-001', 'Dr. Nguyen Van A', '1975-05-15', 'MALE', 'LICENSE-A-001', true),
('doctor-002', 'user-doctor-002', 'hospital-001', 'dept-002', 'Dr. Tran Thi B', '1980-08-20', 'FEMALE', 'LICENSE-B-002', true),
('doctor-003', 'user-doctor-003', 'hospital-002', 'dept-003', 'Dr. Le Van C', '1978-03-10', 'MALE', 'LICENSE-C-003', true),
('doctor-004', 'user-doctor-004', 'hospital-002', 'dept-004', 'Dr. Pham Thi D', '1985-11-25', 'FEMALE', 'LICENSE-D-004', true),
('doctor-005', 'user-doctor-005', 'hospital-003', 'dept-006', 'Dr. Hoang Van E', '1972-07-08', 'MALE', 'LICENSE-E-005', true);

-- =====================================================
-- TEST DATA: TIME_SLOTS TABLE
-- Weekly recurring slots for each doctor
-- =====================================================
INSERT INTO time_slots (timeSlotId, doctorId, dayOfWeek, startTime, endTime, isAvailable, specificDate) VALUES
-- Doctor 001 - Cardiology (Monday, Wednesday, Friday)
('slot-001', 'doctor-001', 'MONDAY', '08:00:00', '08:30:00', true, NULL),
('slot-002', 'doctor-001', 'MONDAY', '08:30:00', '09:00:00', true, NULL),
('slot-003', 'doctor-001', 'MONDAY', '09:00:00', '09:30:00', true, NULL),
('slot-004', 'doctor-001', 'MONDAY', '09:30:00', '10:00:00', false, NULL),
('slot-005', 'doctor-001', 'MONDAY', '10:00:00', '10:30:00', true, NULL),
('slot-006', 'doctor-001', 'WEDNESDAY', '14:00:00', '14:30:00', true, NULL),
('slot-007', 'doctor-001', 'WEDNESDAY', '14:30:00', '15:00:00', true, NULL),
('slot-008', 'doctor-001', 'WEDNESDAY', '15:00:00', '15:30:00', true, NULL),
('slot-009', 'doctor-001', 'FRIDAY', '08:00:00', '08:30:00', true, NULL),
('slot-010', 'doctor-001', 'FRIDAY', '08:30:00', '09:00:00', true, NULL),

-- Doctor 002 - Orthopedics (Tuesday, Thursday)
('slot-011', 'doctor-002', 'TUESDAY', '09:00:00', '09:30:00', true, NULL),
('slot-012', 'doctor-002', 'TUESDAY', '09:30:00', '10:00:00', true, NULL),
('slot-013', 'doctor-002', 'TUESDAY', '10:00:00', '10:30:00', true, NULL),
('slot-014', 'doctor-002', 'TUESDAY', '10:30:00', '11:00:00', false, NULL),
('slot-015', 'doctor-002', 'THURSDAY', '14:00:00', '14:30:00', true, NULL),
('slot-016', 'doctor-002', 'THURSDAY', '14:30:00', '15:00:00', true, NULL),
('slot-017', 'doctor-002', 'THURSDAY', '15:00:00', '15:30:00', true, NULL),

-- Doctor 003 - Pediatrics (Monday, Wednesday, Friday)
('slot-018', 'doctor-003', 'MONDAY', '10:00:00', '10:30:00', true, NULL),
('slot-019', 'doctor-003', 'MONDAY', '10:30:00', '11:00:00', true, NULL),
('slot-020', 'doctor-003', 'WEDNESDAY', '10:00:00', '10:30:00', true, NULL),
('slot-021', 'doctor-003', 'WEDNESDAY', '10:30:00', '11:00:00', true, NULL),
('slot-022', 'doctor-003', 'FRIDAY', '10:00:00', '10:30:00', true, NULL),
('slot-023', 'doctor-003', 'FRIDAY', '10:30:00', '11:00:00', false, NULL),

-- Doctor 004 - Neurology (Tuesday, Thursday, Saturday)
('slot-024', 'doctor-004', 'TUESDAY', '08:00:00', '08:30:00', true, NULL),
('slot-025', 'doctor-004', 'TUESDAY', '08:30:00', '09:00:00', true, NULL),
('slot-026', 'doctor-004', 'THURSDAY', '08:00:00', '08:30:00', true, NULL),
('slot-027', 'doctor-004', 'THURSDAY', '08:30:00', '09:00:00', true, NULL),
('slot-028', 'doctor-004', 'SATURDAY', '09:00:00', '09:30:00', true, NULL),
('slot-029', 'doctor-004', 'SATURDAY', '09:30:00', '10:00:00', true, NULL),

-- Doctor 005 - General Medicine (Every weekday)
('slot-030', 'doctor-005', 'MONDAY', '08:00:00', '08:30:00', true, NULL),
('slot-031', 'doctor-005', 'MONDAY', '08:30:00', '09:00:00', true, NULL),
('slot-032', 'doctor-005', 'TUESDAY', '08:00:00', '08:30:00', true, NULL),
('slot-033', 'doctor-005', 'TUESDAY', '08:30:00', '09:00:00', true, NULL),
('slot-034', 'doctor-005', 'WEDNESDAY', '08:00:00', '08:30:00', true, NULL),
('slot-035', 'doctor-005', 'WEDNESDAY', '08:30:00', '09:00:00', false, NULL),
('slot-036', 'doctor-005', 'THURSDAY', '08:00:00', '08:30:00', true, NULL),
('slot-037', 'doctor-005', 'THURSDAY', '08:30:00', '09:00:00', true, NULL),
('slot-038', 'doctor-005', 'FRIDAY', '08:00:00', '08:30:00', true, NULL),
('slot-039', 'doctor-005', 'FRIDAY', '08:30:00', '09:00:00', true, NULL);

-- =====================================================
USE patientsdb;

-- =====================================================
-- TEST DATA: PATIENTS TABLE
-- =====================================================
INSERT INTO patients (patientId, fullName, userId, birth, registeredAt, gender, insuranceId, emergencyCallingNumber, job, bloodType, heights, weights, isActive) VALUES
('patient-001', 'John Smith', 'user-patient-001', '1990-06-15', NOW(), 'MALE', 'INS-001-2024', '0909111222', 'Software Engineer', 'O', 175.5, 70.0, true),
('patient-002', 'Mary Johnson', 'user-patient-002', '1985-12-20', NOW(), 'FEMALE', 'INS-002-2024', '0909222333', 'Teacher', 'A', 160.0, 55.0, true),
('patient-003', 'Robert Wilson', 'user-patient-003', '1995-03-25', NOW(), 'MALE', 'INS-003-2024', '0909333444', 'Business Owner', 'B', 180.0, 85.0, true),
('patient-004', 'Jennifer Brown', 'user-patient-004', '1988-09-10', NOW(), 'FEMALE', 'INS-004-2024', '0909444555', 'Nurse', 'AB', 165.0, 60.0, true),
('patient-005', 'David Lee', 'user-patient-005', '1992-01-30', NOW(), 'MALE', 'INS-005-2024', '0909555666', 'Accountant', 'A', 172.0, 75.0, true),
('patient-006', 'Sarah Davis', 'user-patient-006', '1993-07-22', NOW(), 'FEMALE', 'INS-006-2024', '0909666777', 'Marketing Manager', 'O', 168.0, 58.0, true),
('patient-007', 'Michael Chen', 'user-patient-007', '1987-04-18', NOW(), 'MALE', 'INS-007-2024', '0909777888', 'Financial Analyst', 'B', 178.0, 80.0, true),
('patient-008', 'Emily Wang', 'user-patient-008', '1991-11-05', NOW(), 'FEMALE', 'INS-008-2024', '0909888999', 'Graphic Designer', 'A', 162.0, 52.0, true);

-- =====================================================
USE appointmentsdb;

-- =====================================================
-- TEST DATA: APPOINTMENTS TABLE
-- Status: PENDING, CONFIRMED, CANCELLED, COMPLETED
-- =====================================================
INSERT INTO appointments (appointmentId, doctorId, doctorName, patientId, patientName, hospitalId, hospitalName, departmentId, departmentName, timeSlotId, appointmentDateTime, status, notes, reason, createdAt, updatedAt) VALUES
-- Confirmed appointment
('apt-001', 'doctor-001', 'Dr. Nguyen Van A', 'patient-001', 'John Smith', 'hospital-001', 'Smart Health Central Hospital', 'dept-001', 'Cardiology', 'slot-001', '2026-04-21 08:00:00', 'CONFIRMED', 'First consultation', 'Heart palpitations', NOW(), NOW()),

-- Pending appointments
('apt-002', 'doctor-002', 'Dr. Tran Thi B', 'patient-002', 'Mary Johnson', 'hospital-001', 'Smart Health Central Hospital', 'dept-002', 'Orthopedics', 'slot-011', '2026-04-22 09:00:00', 'PENDING', 'Follow-up visit', 'Knee pain', NOW(), NOW()),
('apt-003', 'doctor-003', 'Dr. Le Van C', 'patient-003', 'Robert Wilson', 'hospital-002', 'Smart Health District Hospital', 'dept-003', 'Pediatrics', 'slot-018', '2026-04-23 10:00:00', 'PENDING', 'Child checkup', 'Annual physical exam', NOW(), NOW()),

-- Cancelled appointment
('apt-004', 'doctor-004', 'Dr. Pham Thi D', 'patient-004', 'Jennifer Brown', 'hospital-002', 'Smart Health District Hospital', 'dept-004', 'Neurology', 'slot-024', '2026-04-20 08:00:00', 'CANCELLED', 'Schedule conflict', 'Migraine headaches', NOW(), NOW()),

-- Completed appointments (with medical history)
('apt-005', 'doctor-005', 'Dr. Hoang Van E', 'patient-005', 'David Lee', 'hospital-003', 'Smart Health International Clinic', 'dept-006', 'General Medicine', 'slot-030', '2026-04-15 08:00:00', 'COMPLETED', 'Routine checkup', 'Annual health screening', NOW(), NOW()),
('apt-006', 'doctor-001', 'Dr. Nguyen Van A', 'patient-006', 'Sarah Davis', 'hospital-001', 'Smart Health Central Hospital', 'dept-001', 'Cardiology', 'slot-002', '2026-04-10 08:30:00', 'COMPLETED', 'Follow-up', 'Blood pressure monitoring', NOW(), NOW()),

-- Additional pending appointments
('apt-007', 'doctor-002', 'Dr. Tran Thi B', 'patient-007', 'Michael Chen', 'hospital-001', 'Smart Health Central Hospital', 'dept-002', 'Orthopedics', 'slot-012', '2026-04-24 09:30:00', 'PENDING', 'Sports injury', 'Ankle sprain', NOW(), NOW()),
('apt-008', 'doctor-003', 'Dr. Le Van C', 'patient-008', 'Emily Wang', 'hospital-002', 'Smart Health District Hospital', 'dept-003', 'Pediatrics', 'slot-020', '2026-04-25 10:30:00', 'PENDING', 'Consultation', 'Allergy concerns', NOW(), NOW());

-- =====================================================
USE ehrdb;

-- =====================================================
-- TEST DATA: MEDICAL_VISITS TABLE
-- =====================================================
INSERT INTO medical_visits (visitId, patientId, doctorId, visitDate, hospital, department, diagnosis, createdAt, updatedAt) VALUES
('visit-001', 'patient-005', 'doctor-005', '2026-04-15 08:00:00', 'Smart Health International Clinic', 'General Medicine', 'Patient presents for annual health screening. Overall health status: Good. Blood pressure: 120/80 mmHg. Recommended maintaining healthy lifestyle with regular exercise.', NOW(), NOW()),
('visit-002', 'patient-006', 'doctor-001', '2026-04-10 08:30:00', 'Smart Health Central Hospital', 'Cardiology', 'Follow-up for hypertension. Blood pressure well controlled at 125/82 mmHg. Continue current medication. Next appointment in 3 months.', NOW(), NOW()),
('visit-003', 'patient-001', 'doctor-001', '2026-03-20 09:00:00', 'Smart Health Central Hospital', 'Cardiology', 'Initial consultation for palpitations. ECG shows normal sinus rhythm. Recommended lifestyle modifications and stress management. Follow-up in 2 weeks.', NOW(), NOW());

-- =====================================================
-- TEST DATA: PRESCRIPTIONS TABLE
-- =====================================================
INSERT INTO prescriptions (prescriptionId, visitId, patientId, medicationName, dosage, frequency, quantity, instructions, prescribedDate, createdAt) VALUES
('rx-001', 'visit-001', 'patient-005', 'Vitamin D3', '1000 IU', 'Once daily', 30, 'Take with food in the morning', '2026-04-15', NOW()),
('rx-002', 'visit-001', 'patient-005', 'Multivitamin', '1 tablet', 'Once daily', 30, 'Take after breakfast', '2026-04-15', NOW()),
('rx-003', 'visit-002', 'patient-006', 'Amlodipine', '5mg', 'Once daily', 90, 'Take in the morning with water. Monitor blood pressure regularly.', '2026-04-10', NOW()),
('rx-004', 'visit-002', 'patient-006', 'Lisinopril', '10mg', 'Once daily', 90, 'Take at the same time each day. Avoid potassium supplements.', '2026-04-10', NOW()),
('rx-005', 'visit-003', 'patient-001', 'Magnesium Glycinate', '400mg', 'Once daily', 14, 'Take before bedtime for heart health support', '2026-03-20', NOW()),
('rx-006', 'visit-003', 'patient-001', 'CoQ10', '100mg', 'Once daily', 30, 'Take with fatty meal for better absorption', '2026-03-20', NOW());
