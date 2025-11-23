# Hướng Dẫn Cài Đặt Database

## 📋 Tổng Quan

Hệ thống sử dụng các databases riêng biệt cho mỗi microservice:

| Service | Database | Port | Tables |
|---------|----------|------|--------|
| User Service | usersdb | 3309 | users, roles, permissions |
| Patient Service | patientsdb | 3311 | patients |
| **Doctor Service** | **doctorsdb** | **3310** | **doctors, time_slots** |
| EHR Service | ehrdb | 3312 | medical_records, prescriptions |
| Hospital Service | hospitalsdb | 3314 | hospitals, departments |
| **Appointment Service** | **appointmentsdb** | **3313** | **appointments, appointment_history** |

---

## 🚀 Cách 1: Chạy Scripts Thủ Công

### Bước 1: Tạo Databases và Users
```bash
# Connect to MySQL root
mysql -u root -p

# Hoặc nếu dùng Docker MySQL container
docker exec -it mysql-container mysql -u root -p
```

```sql
-- Chạy script tạo databases
source /path/to/docker-init/init-databases.sql;

-- Hoặc
\. /path/to/docker-init/init-databases.sql
```

### Bước 2: Khởi tạo Doctor Service Database
```bash
# Connect to doctorsdb
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb

# Hoặc qua Docker
docker exec -i mysql-container mysql -u admin -p123456 doctorsdb < doctor-service/init-database.sql
```

```sql
-- Trong MySQL shell
source /path/to/doctor-service/init-database.sql;
```

### Bước 3: Khởi tạo Appointment Service Database
```bash
# Connect to appointmentsdb
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb

# Hoặc qua Docker
docker exec -i mysql-container mysql -u admin -p123456 appointmentsdb < appointment-service/init-database.sql
```

```sql
-- Trong MySQL shell
source /path/to/appointment-service/init-database.sql;
```

---

## 🐳 Cách 2: Sử dụng Docker Compose (Khuyến nghị)

### Docker Compose với Volume Mount

Tạo/cập nhật `docker-compose.yml`:

```yaml
version: '3.8'

services:
  # Doctor Service MySQL
  mysql-doctor:
    image: mysql:8.0
    container_name: mysql-doctor-service
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: doctorsdb
      MYSQL_USER: admin
      MYSQL_PASSWORD: 123456
    ports:
      - "3310:3306"
    volumes:
      - ./doctor-service/init-database.sql:/docker-entrypoint-initdb.d/init.sql
      - doctor_data:/var/lib/mysql
    networks:
      - healthcare-network

  # Appointment Service MySQL
  mysql-appointment:
    image: mysql:8.0
    container_name: mysql-appointment-service
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: appointmentsdb
      MYSQL_USER: admin
      MYSQL_PASSWORD: 123456
    ports:
      - "3313:3306"
    volumes:
      - ./appointment-service/init-database.sql:/docker-entrypoint-initdb.d/init.sql
      - appointment_data:/var/lib/mysql
    networks:
      - healthcare-network

volumes:
  doctor_data:
  appointment_data:

networks:
  healthcare-network:
    driver: bridge
```

### Chạy Docker Compose
```bash
# Start services
docker-compose up -d mysql-doctor mysql-appointment

# Check logs
docker-compose logs -f mysql-doctor
docker-compose logs -f mysql-appointment

# Verify databases
docker exec -it mysql-doctor-service mysql -u admin -p123456 -e "SHOW DATABASES;"
docker exec -it mysql-appointment-service mysql -u admin -p123456 -e "SHOW DATABASES;"
```

---

## ✅ Kiểm Tra Cài Đặt

### Doctor Service Database

```bash
# Connect
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb

# Or via Docker
docker exec -it mysql-doctor-service mysql -u admin -p123456 doctorsdb
```

```sql
-- Check tables
SHOW TABLES;
-- Output: doctors, time_slots

-- Check doctors
SELECT doctor_id, full_name, department FROM doctors;

-- Check time slots
SELECT 
    time_slot_id, 
    doctor_id, 
    day_of_week, 
    start_time, 
    end_time, 
    is_available 
FROM time_slots 
LIMIT 10;

-- Check available slots view
SELECT * FROM available_time_slots WHERE doctor_id = 1 LIMIT 5;

-- Count time slots by doctor
SELECT 
    doctor_id, 
    COUNT(*) as total_slots,
    SUM(CASE WHEN is_available = TRUE THEN 1 ELSE 0 END) as available_slots
FROM time_slots
GROUP BY doctor_id;
```

### Appointment Service Database

```bash
# Connect
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb

# Or via Docker
docker exec -it mysql-appointment-service mysql -u admin -p123456 appointmentsdb
```

```sql
-- Check tables
SHOW TABLES;
-- Output: appointments, appointment_history

-- Check appointments
SELECT 
    appointment_id,
    doctor_name,
    patient_name,
    appointment_date_time,
    status
FROM appointments;

-- Check upcoming appointments
SELECT * FROM upcoming_appointments;

-- Check today's appointments
SELECT * FROM today_appointments;

-- Check appointment statistics by doctor
SELECT * FROM appointment_stats_by_doctor;

-- Check appointment statistics by patient
SELECT * FROM appointment_stats_by_patient;

-- Test stored procedure
CALL confirm_appointment(2);
SELECT * FROM appointments WHERE appointment_id = 2;
```

---

## 📊 Sample Data Overview

### Doctor Service

**Doctors (5 records):**
- Dr. Nguyen Van A - Cardiology (40 time slots, Mon-Fri 8:00-12:00)
- Dr. Tran Thi B - Neurology (18 time slots, Mon/Wed/Fri 14:00-17:00)
- Dr. Le Van C - Pediatrics
- Dr. Pham Thi D - Dermatology
- Dr. Hoang Van E - Orthopedics

**Time Slots:** 58 slots tổng cộng cho 2 bác sĩ đầu tiên

### Appointment Service

**Appointments (8 records):**
- 2 CONFIRMED (upcoming)
- 2 PENDING
- 1 COMPLETED (past)
- 1 CANCELLED (past)
- 1 NO_SHOW (past)
- 2 Future appointments

---

## 🔧 Useful SQL Queries

### Doctor Service Queries

```sql
-- Lấy lịch trống của bác sĩ 1 vào thứ 2
SELECT * FROM time_slots
WHERE doctor_id = 1 
  AND day_of_week = 'MONDAY'
  AND is_available = TRUE
ORDER BY start_time;

-- Đếm số slot available theo ngày trong tuần
SELECT 
    day_of_week,
    COUNT(*) as available_slots
FROM time_slots
WHERE is_available = TRUE
GROUP BY day_of_week
ORDER BY FIELD(day_of_week, 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');

-- Tìm bác sĩ có nhiều slot trống nhất
SELECT 
    d.doctor_id,
    d.full_name,
    d.department,
    COUNT(ts.time_slot_id) as available_slots
FROM doctors d
LEFT JOIN time_slots ts ON d.doctor_id = ts.doctor_id AND ts.is_available = TRUE
WHERE d.is_active = TRUE
GROUP BY d.doctor_id, d.full_name, d.department
ORDER BY available_slots DESC;
```

### Appointment Service Queries

```sql
-- Lấy tất cả appointments của bệnh nhân 101
SELECT * FROM appointments
WHERE patient_id = 101
ORDER BY appointment_date_time DESC;

-- Lấy appointments trong tuần này
SELECT * FROM appointments
WHERE YEARWEEK(appointment_date_time, 1) = YEARWEEK(CURDATE(), 1)
  AND status IN ('PENDING', 'CONFIRMED')
ORDER BY appointment_date_time;

-- Thống kê theo status
SELECT 
    status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM appointments), 2) as percentage
FROM appointments
GROUP BY status
ORDER BY count DESC;

-- Tìm bệnh nhân hay hủy lịch
SELECT 
    patient_id,
    patient_name,
    cancelled_count,
    no_show_count,
    (cancelled_count + no_show_count) as total_issues
FROM appointment_stats_by_patient
WHERE (cancelled_count + no_show_count) > 0
ORDER BY total_issues DESC;

-- Lịch hẹn trùng (conflict detection)
SELECT 
    a1.appointment_id as apt1,
    a2.appointment_id as apt2,
    a1.doctor_id,
    a1.appointment_date_time
FROM appointments a1
INNER JOIN appointments a2 
    ON a1.doctor_id = a2.doctor_id
    AND a1.appointment_date_time = a2.appointment_date_time
    AND a1.appointment_id < a2.appointment_id
WHERE a1.status IN ('PENDING', 'CONFIRMED')
  AND a2.status IN ('PENDING', 'CONFIRMED');
```

---

## 🛠️ Maintenance Commands

### Backup Databases

```bash
# Doctor Service
mysqldump -u admin -p123456 -h localhost -P 3310 doctorsdb > doctorsdb_backup_$(date +%Y%m%d).sql

# Appointment Service
mysqldump -u admin -p123456 -h localhost -P 3313 appointmentsdb > appointmentsdb_backup_$(date +%Y%m%d).sql

# Via Docker
docker exec mysql-doctor-service mysqldump -u admin -p123456 doctorsdb > doctorsdb_backup.sql
docker exec mysql-appointment-service mysqldump -u admin -p123456 appointmentsdb > appointmentsdb_backup.sql
```

### Restore Databases

```bash
# Doctor Service
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb < doctorsdb_backup.sql

# Appointment Service
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb < appointmentsdb_backup.sql

# Via Docker
docker exec -i mysql-doctor-service mysql -u admin -p123456 doctorsdb < doctorsdb_backup.sql
docker exec -i mysql-appointment-service mysql -u admin -p123456 appointmentsdb < appointmentsdb_backup.sql
```

### Reset Database

```bash
# Drop and recreate
mysql -u admin -p123456 -h localhost -P 3310 -e "DROP DATABASE IF EXISTS doctorsdb; CREATE DATABASE doctorsdb;"
mysql -u admin -p123456 -h localhost -P 3310 doctorsdb < doctor-service/init-database.sql

mysql -u admin -p123456 -h localhost -P 3313 -e "DROP DATABASE IF EXISTS appointmentsdb; CREATE DATABASE appointmentsdb;"
mysql -u admin -p123456 -h localhost -P 3313 appointmentsdb < appointment-service/init-database.sql
```

---

## 🚨 Troubleshooting

### Problem: Cannot connect to MySQL

**Solution:**
```bash
# Check if MySQL is running
docker ps | grep mysql

# Check MySQL logs
docker logs mysql-doctor-service
docker logs mysql-appointment-service

# Restart MySQL containers
docker restart mysql-doctor-service
docker restart mysql-appointment-service
```

### Problem: Permission denied

**Solution:**
```sql
-- Grant permissions
GRANT ALL PRIVILEGES ON doctorsdb.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON appointmentsdb.* TO 'admin'@'%';
FLUSH PRIVILEGES;
```

### Problem: Tables not created

**Solution:**
```bash
# Check if init script was executed
docker logs mysql-doctor-service | grep "init.sql"

# Manually run init script
docker exec -i mysql-doctor-service mysql -u admin -p123456 doctorsdb < doctor-service/init-database.sql
```

### Problem: Character encoding issues

**Solution:**
```sql
-- Check database charset
SHOW CREATE DATABASE doctorsdb;
SHOW CREATE DATABASE appointmentsdb;

-- If needed, alter database charset
ALTER DATABASE doctorsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER DATABASE appointmentsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 📚 References

- [Doctor Service Init SQL](doctor-service/init-database.sql)
- [Appointment Service Init SQL](appointment-service/init-database.sql)
- [Main Init Databases SQL](../docker-init/init-databases.sql)
- [API Documentation](APPOINTMENT_TIMESLOT_GUIDE.md)

---

## ✨ Next Steps

1. ✅ Chạy init scripts
2. ✅ Verify data
3. 🚀 Start Spring Boot services
4. 🧪 Test APIs với Postman/curl
5. 📱 Integrate với mobile app

---

**Chúc bạn setup thành công! 🎉**

