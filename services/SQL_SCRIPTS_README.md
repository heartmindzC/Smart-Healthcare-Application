# SQL Scripts Summary

## 📁 Files Created

### 1. `../docker-init/init-databases.sql` (Updated)
**Mục đích:** Tạo tất cả databases và grant permissions

**Thay đổi:**
- ✅ Thêm `appointmentsdb` database
- ✅ Thêm permissions cho appointmentsdb

**Databases được tạo:**
- usersdb
- patientsdb
- doctorsdb
- ehrdb
- hospitalsdb
- **appointmentsdb** ← MỚI

---

### 2. `doctor-service/init-database.sql` (New)
**Mục đích:** Khởi tạo Doctor Service database với time slots

**Bao gồm:**

#### Tables:
1. **doctors** - Thông tin bác sĩ
   - doctor_id, user_id, hospital_id
   - full_name, department, license_id
   - gender, birth, registration_at
   - is_active

2. **time_slots** ← MỚI - Khung giờ làm việc
   - time_slot_id, doctor_id
   - day_of_week (MONDAY-SUNDAY)
   - start_time, end_time
   - is_available (TRUE/FALSE)
   - specific_date (NULL = recurring, có giá trị = ngày cụ thể)

#### Sample Data:
- 5 doctors
- 58 time slots (cho 2 bác sĩ)
  - Doctor 1: Mon-Fri, 8:00-12:00 (40 slots)
  - Doctor 2: Mon/Wed/Fri, 14:00-17:00 (18 slots)

#### Views:
- `available_time_slots` - Time slots còn trống

#### Indexes:
- doctor_id, day_of_week, specific_date
- is_available, doctor_available
- Composite indexes cho performance

---

### 3. `appointment-service/init-database.sql` (New)
**Mục đích:** Khởi tạo Appointment Service database

**Bao gồm:**

#### Tables:
1. **appointments** - Lịch hẹn khám bệnh
   - appointment_id
   - doctor_id, doctor_name
   - patient_id, patient_name
   - hospital_id, hospital_name
   - time_slot_id (reference to doctor service)
   - appointment_date_time
   - status (PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW)
   - notes, reason
   - created_at, updated_at

2. **appointment_history** - Lịch sử thay đổi
   - history_id, appointment_id
   - old_status, new_status
   - old_date_time, new_date_time
   - changed_by, change_reason

#### Sample Data:
- 8 appointments với các status khác nhau
  - 2 CONFIRMED (upcoming)
  - 2 PENDING
  - 1 COMPLETED
  - 1 CANCELLED
  - 1 NO_SHOW
  - 1 future appointment

#### Views:
- `upcoming_appointments` - Lịch hẹn sắp tới
- `today_appointments` - Lịch hẹn hôm nay
- `appointment_stats_by_doctor` - Thống kê theo bác sĩ
- `appointment_stats_by_patient` - Thống kê theo bệnh nhân

#### Stored Procedures:
- `cancel_appointment(appointment_id, reason)` - Hủy lịch hẹn
- `confirm_appointment(appointment_id)` - Xác nhận lịch hẹn
- `complete_appointment(appointment_id, notes)` - Hoàn thành lịch hẹn

#### Triggers:
- `after_appointment_status_update` - Auto log thay đổi status

#### Indexes:
- doctor_id, patient_id, hospital_id
- time_slot_id, status, appointment_date_time
- Composite indexes: (doctor, status), (patient, status), (doctor, date), etc.

---

## 🚀 Quick Start

### Option 1: Chạy thủ công
```bash
# 1. Tạo databases
mysql -u root -p < docker-init/init-databases.sql

# 2. Init Doctor Service
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb < doctor-service/init-database.sql

# 3. Init Appointment Service
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb < appointment-service/init-database.sql
```

### Option 2: Docker (Khuyến nghị)
```bash
# Via docker-compose with volume mount
docker-compose up -d mysql-doctor mysql-appointment

# Hoặc chạy trực tiếp
docker exec -i mysql-doctor-service mysql -u admin -p123456 doctorsdb < doctor-service/init-database.sql
docker exec -i mysql-appointment-service mysql -u admin -p123456 appointmentsdb < appointment-service/init-database.sql
```

---

## ✅ Verification

### Check Doctor Service
```sql
-- Connect
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb

-- Verify
SHOW TABLES;  -- doctors, time_slots
SELECT COUNT(*) FROM doctors;  -- 5
SELECT COUNT(*) FROM time_slots;  -- 58
SELECT * FROM available_time_slots LIMIT 5;
```

### Check Appointment Service
```sql
-- Connect
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb

-- Verify
SHOW TABLES;  -- appointments, appointment_history
SELECT COUNT(*) FROM appointments;  -- 8
SELECT * FROM upcoming_appointments;
SELECT * FROM appointment_stats_by_doctor;
```

---

## 📊 Database Schema Overview

```
doctorsdb
├── doctors (5 records)
│   └── FK: user_id → usersdb.users
│   └── FK: hospital_id → hospitalsdb.hospitals
│
└── time_slots (58 records)
    └── FK: doctor_id → doctors.doctor_id

appointmentsdb
├── appointments (8 records)
│   ├── REF: doctor_id → doctorsdb.doctors (no FK, microservices)
│   ├── REF: patient_id → patientsdb.patients (no FK)
│   ├── REF: hospital_id → hospitalsdb.hospitals (no FK)
│   └── REF: time_slot_id → doctorsdb.time_slots (no FK)
│
└── appointment_history (auto-populated by trigger)
```

**Note:** Không dùng Foreign Keys giữa các services khác nhau (microservices pattern)

---

## 🎯 Key Features

### Doctor Service - Time Slots
✅ **Recurring Slots** - Lặp lại theo thứ trong tuần (specific_date = NULL)  
✅ **Specific Date Slots** - Override cho ngày cụ thể  
✅ **Availability Tracking** - is_available flag  
✅ **Flexible Schedule** - Mỗi bác sĩ tự định lịch  
✅ **Performance Optimized** - Multiple indexes  

### Appointment Service
✅ **Rich Status** - 5 trạng thái (PENDING → CONFIRMED → COMPLETED)  
✅ **Denormalized Data** - Lưu names để giảm joins  
✅ **History Tracking** - Auto log mọi thay đổi  
✅ **Statistics Views** - Ready-made reports  
✅ **Stored Procedures** - Business logic ở DB level  
✅ **Automatic Triggers** - Tự động log changes  

---

## 📝 Common Queries

### Get Available Slots
```sql
-- Doctor 1, thứ 2
SELECT * FROM time_slots
WHERE doctor_id = 1 
  AND day_of_week = 'MONDAY'
  AND is_available = TRUE;
```

### Get Patient Appointments
```sql
SELECT * FROM appointments
WHERE patient_id = 101
ORDER BY appointment_date_time DESC;
```

### Get Today's Schedule
```sql
SELECT * FROM today_appointments;
```

### Cancel Appointment
```sql
CALL cancel_appointment(1, 'Bệnh nhân bận việc gấp');
```

---

## 📚 Documentation

- **[DATABASE_SETUP_GUIDE.md](DATABASE_SETUP_GUIDE.md)** - Chi tiết setup & troubleshooting
- **[APPOINTMENT_TIMESLOT_GUIDE.md](APPOINTMENT_TIMESLOT_GUIDE.md)** - API usage guide
- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Implementation overview

---

## 🔄 Migration from Old Schema

Nếu đã có data cũ:

```sql
-- Backup first!
mysqldump -u admin -p123456 doctorsdb > backup_old_doctorsdb.sql
mysqldump -u admin -p123456 appointmentsdb > backup_old_appointmentsdb.sql

-- Run new scripts
-- ... your init scripts ...

-- Migrate old data if needed
INSERT INTO doctors (user_id, hospital_id, full_name, ...)
SELECT user_id, hospital_id, full_name, ...
FROM old_doctors_backup;
```

---

**Happy coding! 🚀**

