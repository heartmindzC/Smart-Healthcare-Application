# Hướng Dẫn Sử Dụng Time Slots và Appointments

## Tổng Quan Kiến Trúc

### Doctor Service
- **Port**: 8082
- **Database**: doctorsdb (port 3310)
- **Quản lý**: Bác sĩ và khung giờ làm việc (TimeSlots)

### Appointment Service  
- **Port**: 8085
- **Database**: appointmentsdb (port 3313)
- **Quản lý**: Lịch hẹn của bệnh nhân

---

## Cấu Trúc Dữ Liệu

### Time Slot (Doctor Service)
```java
{
  "timeSlotId": 1,
  "doctorId": 10,
  "dayOfWeek": "MONDAY",           // MONDAY, TUESDAY, ...
  "startTime": "08:00",
  "endTime": "08:30",
  "isAvailable": true,
  "specificDate": null             // null = lặp lại hàng tuần
}
```

### Appointment (Appointment Service)
```java
{
  "appointmentId": 1,
  "doctorId": 10,
  "doctorName": "Dr. Nguyen Van A",
  "patientId": 5,
  "patientName": "Tran Thi B",
  "hospitalId": 1,
  "hospitalName": "Bach Mai Hospital",
  "timeSlotId": 1,                 // Reference đến TimeSlot
  "appointmentDateTime": "2024-11-25T08:00:00",
  "status": "PENDING",             // PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
  "notes": "Khám định kỳ",
  "reason": "Đau đầu",
  "createdAt": "2024-11-23T10:00:00",
  "updatedAt": "2024-11-23T10:00:00"
}
```

---

## API Endpoints

### 1. Doctor Service - Time Slots

#### Lấy tất cả time slots
```bash
GET http://localhost:8082/time-slots/all
```

#### Lấy time slots của một bác sĩ
```bash
GET http://localhost:8082/time-slots/doctor/{doctorId}
```

#### Lấy available time slots của bác sĩ
```bash
GET http://localhost:8082/time-slots/doctor/{doctorId}/available
```

#### Lấy available time slots theo ngày cụ thể
```bash
GET http://localhost:8082/time-slots/doctor/{doctorId}/available-by-date?date=2024-11-25
```

#### Tạo time slot mới
```bash
POST http://localhost:8082/time-slots/create
Content-Type: application/json

{
  "doctorId": 10,
  "dayOfWeek": "MONDAY",
  "startTime": "08:00",
  "endTime": "08:30",
  "isAvailable": true,
  "specificDate": null
}
```

#### Cập nhật time slot
```bash
PUT http://localhost:8082/time-slots/update/{timeSlotId}
Content-Type: application/json

{
  "isAvailable": false
}
```

#### Cập nhật availability
```bash
PATCH http://localhost:8082/time-slots/update-availability/{timeSlotId}?isAvailable=false
```

#### Xóa time slot
```bash
DELETE http://localhost:8082/time-slots/delete/{timeSlotId}
```

---

### 2. Appointment Service

#### Lấy tất cả appointments
```bash
GET http://localhost:8085/appointments/all
```

#### Lấy appointment theo ID
```bash
GET http://localhost:8085/appointments/{appointmentId}
```

#### Lấy appointments của bệnh nhân
```bash
GET http://localhost:8085/appointments/patient/{patientId}
```

#### Lấy appointments của bác sĩ
```bash
GET http://localhost:8085/appointments/doctor/{doctorId}
```

#### Lấy appointments theo status
```bash
GET http://localhost:8085/appointments/status/{status}
# status: PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
```

#### Lấy appointments theo khoảng thời gian
```bash
GET http://localhost:8085/appointments/date-range?start=2024-11-25T00:00:00&end=2024-11-30T23:59:59
```

#### Tạo appointment mới
```bash
POST http://localhost:8085/appointments/create
Content-Type: application/json

{
  "doctorId": 10,
  "doctorName": "Dr. Nguyen Van A",
  "patientId": 5,
  "patientName": "Tran Thi B",
  "hospitalId": 1,
  "hospitalName": "Bach Mai Hospital",
  "timeSlotId": 1,
  "appointmentDateTime": "2024-11-25T08:00:00",
  "notes": "Khám định kỳ",
  "reason": "Đau đầu"
}
```

#### Cập nhật appointment
```bash
PUT http://localhost:8085/appointments/update/{appointmentId}
Content-Type: application/json

{
  "notes": "Cập nhật ghi chú"
}
```

#### Xác nhận appointment
```bash
PATCH http://localhost:8085/appointments/confirm/{appointmentId}
```

#### Hủy appointment
```bash
PATCH http://localhost:8085/appointments/cancel/{appointmentId}
```

#### Hoàn thành appointment
```bash
PATCH http://localhost:8085/appointments/complete/{appointmentId}
```

#### Xóa appointment
```bash
DELETE http://localhost:8085/appointments/delete/{appointmentId}
```

---

## Workflow Đặt Lịch

### Bước 1: Bác sĩ tạo lịch làm việc
```bash
# Tạo time slots cho thứ 2-6, mỗi slot 30 phút
POST http://localhost:8082/time-slots/create

# Tạo slot cho thứ 2, 8:00-8:30
{
  "doctorId": 10,
  "dayOfWeek": "MONDAY",
  "startTime": "08:00",
  "endTime": "08:30",
  "isAvailable": true
}

# Tạo slot cho thứ 2, 8:30-9:00
{
  "doctorId": 10,
  "dayOfWeek": "MONDAY",
  "startTime": "08:30",
  "endTime": "09:00",
  "isAvailable": true
}

# ... và tiếp tục cho các ngày khác
```

### Bước 2: Bệnh nhân xem lịch trống
```bash
# Xem lịch trống của bác sĩ ngày 25/11/2024
GET http://localhost:8082/time-slots/doctor/10/available-by-date?date=2024-11-25
```

### Bước 3: Bệnh nhân đặt lịch
```bash
POST http://localhost:8085/appointments/create
{
  "doctorId": 10,
  "doctorName": "Dr. Nguyen Van A",
  "patientId": 5,
  "patientName": "Tran Thi B",
  "hospitalId": 1,
  "hospitalName": "Bach Mai Hospital",
  "timeSlotId": 1,
  "appointmentDateTime": "2024-11-25T08:00:00",
  "reason": "Khám sức khỏe"
}
```

### Bước 4: Đánh dấu time slot đã được đặt
```bash
# (Nên tự động xử lý trong code)
PATCH http://localhost:8082/time-slots/update-availability/1?isAvailable=false
```

### Bước 5: Bác sĩ xác nhận lịch
```bash
PATCH http://localhost:8085/appointments/confirm/1
```

### Nếu hủy lịch
```bash
# 1. Hủy appointment
PATCH http://localhost:8085/appointments/cancel/1

# 2. Đánh dấu time slot available trở lại
PATCH http://localhost:8082/time-slots/update-availability/1?isAvailable=true
```

---

## Database Schema

### doctorsdb.time_slots
```sql
CREATE TABLE time_slots (
    time_slot_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week VARCHAR(20),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    specific_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### appointmentsdb.appointments
```sql
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    doctor_name VARCHAR(255),
    patient_id INT NOT NULL,
    patient_name VARCHAR(255),
    hospital_id INT NOT NULL,
    hospital_name VARCHAR(255),
    time_slot_id INT,
    appointment_date_time TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    notes TEXT,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## Lưu Ý Quan Trọng

1. **Time Slot thuộc về Doctor Service** - vì bác sĩ sở hữu lịch làm việc
2. **Appointment Service chỉ reference** - lưu timeSlotId để tham chiếu
3. **Denormalization** - Lưu appointmentDateTime trong Appointment để query nhanh
4. **Microservices Communication** - Cần implement RestTemplate/WebClient để gọi giữa services
5. **Transaction Handling** - Khi tạo appointment, cần đảm bảo time slot được mark unavailable
6. **Consistency** - Khi hủy appointment, phải restore time slot về available

---

## TODO - Cải Tiến

### 1. Inter-service Communication
Implement RestTemplate hoặc WebClient trong Appointment Service để:
- Verify time slot còn available trước khi tạo appointment
- Auto mark time slot unavailable khi tạo appointment
- Auto restore time slot khi hủy appointment

### 2. Validation
- Kiểm tra time slot có tồn tại không
- Kiểm tra time slot còn available không
- Kiểm tra conflict với các appointments khác

### 3. Notification
- Gửi thông báo cho bệnh nhân khi appointment được confirm
- Reminder trước 1 ngày

### 4. Business Rules
- Bệnh nhân không được đặt quá X appointments cùng lúc
- Không được đặt appointment trong quá khứ
- Hủy appointment phải trước X giờ

---

## Chạy Services

```bash
# Chạy Doctor Service
cd doctor-service
mvn spring-boot:run

# Chạy Appointment Service
cd appointment-service
mvn spring-boot:run
```

Hoặc sử dụng Docker:
```bash
docker-compose up doctor-service appointment-service
```

