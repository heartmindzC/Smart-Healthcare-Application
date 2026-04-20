# API Gateway - Quick Reference Guide

**Base URL**: `http://localhost:8085`

---

## 🏥 Hospital Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/hospitals/all` | Lấy tất cả bệnh viện |
| GET | `/api/hospitals/get-hospital/{id}` | Lấy bệnh viện theo ID |
| GET | `/api/hospitals/get-hospital-by-name/{name}` | Lấy bệnh viện theo tên |
| GET | `/api/hospitals/search-by-address/{address}` | Tìm theo địa chỉ |
| POST | `/api/hospitals/search` | Tìm kiếm nâng cao |
| POST | `/api/hospitals/create` | Tạo bệnh viện mới |
| PUT | `/api/hospitals/update/{id}` | Cập nhật bệnh viện |
| DELETE | `/api/hospitals/delete/{id}` | Xóa bệnh viện |

---

## 🏢 Department Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/departments/all` | Lấy tất cả khoa/phòng ban |
| GET | `/api/departments/get-department/{id}` | Lấy khoa/phòng ban theo ID |
| GET | `/api/departments/get-department-by-name/{name}` | Lấy theo tên |
| GET | `/api/departments/get-departments-by-hospital/{hospitalId}` | Lấy theo bệnh viện |
| GET | `/api/departments/search-by-name/{name}` | Tìm kiếm theo tên |
| POST | `/api/departments/search` | Tìm kiếm nâng cao |
| POST | `/api/departments/create` | Tạo khoa/phòng ban mới |
| PUT | `/api/departments/update/{id}` | Cập nhật khoa/phòng ban |
| DELETE | `/api/departments/delete/{id}` | Xóa khoa/phòng ban |

---

## 👤 User Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/users/**` | Tất cả endpoints của User Service |

**Service Port**: 8080

---

## 👨‍⚕️ Patient Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/patients/**` | Tất cả endpoints của Patient Service |

**Service Port**: 8081

---

## 🩺 Doctor Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/doctors/**` | Tất cả endpoints của Doctor Service |

**Service Port**: 8082

---

## ⏰ Time Slot Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/time-slots/all` | Lấy tất cả time slots |
| GET | `/api/time-slots/doctor/{doctorId}` | Lấy time slots của bác sĩ |
| GET | `/api/time-slots/doctor/{doctorId}/available` | Lấy time slots còn trống |
| GET | `/api/time-slots/doctor/{doctorId}/available-by-date?date={date}` | Lấy lịch trống theo ngày |
| POST | `/api/time-slots/create` | Tạo time slot mới |
| PUT | `/api/time-slots/update/{id}` | Cập nhật time slot |
| PATCH | `/api/time-slots/update-availability/{id}?isAvailable={bool}` | Cập nhật availability |
| DELETE | `/api/time-slots/delete/{id}` | Xóa time slot |

**Service Port**: 8082

---

## 📅 Appointment Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/appointments/all` | Lấy tất cả appointments |
| GET | `/api/appointments/{id}` | Lấy appointment theo ID |
| GET | `/api/appointments/patient/{patientId}` | Lấy appointments của bệnh nhân |
| GET | `/api/appointments/doctor/{doctorId}` | Lấy appointments của bác sĩ |
| GET | `/api/appointments/hospital/{hospitalId}` | Lấy appointments của bệnh viện |
| GET | `/api/appointments/status/{status}` | Lấy theo status |
| GET | `/api/appointments/patient/{patientId}/status/{status}` | Lấy theo bệnh nhân & status |
| GET | `/api/appointments/doctor/{doctorId}/status/{status}` | Lấy theo bác sĩ & status |
| GET | `/api/appointments/date-range?start={start}&end={end}` | Lấy theo khoảng thời gian |
| POST | `/api/appointments/create` | Tạo appointment mới |
| PUT | `/api/appointments/update/{id}` | Cập nhật appointment |
| PATCH | `/api/appointments/update-status/{id}?status={status}` | Cập nhật status |
| PATCH | `/api/appointments/confirm/{id}` | Xác nhận appointment |
| PATCH | `/api/appointments/cancel/{id}` | Hủy appointment |
| PATCH | `/api/appointments/complete/{id}` | Hoàn thành appointment |
| DELETE | `/api/appointments/delete/{id}` | Xóa appointment |

**Service Port**: 8086

---

## 📋 EHR Service

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/ehr/**` | Tất cả endpoints của EHR Service |

**Service Port**: 8083

---

## 📝 Request/Response Examples

### Create Hospital
```bash
POST /api/hospitals/create
Content-Type: application/json

{
  "hospitalName": "Bệnh viện Mới",
  "hospitalAddress": "123 Đường XYZ",
  "hospitalPhone": "028-9999-8888",
  "hospitalEmail": "info@newhospital.vn"
}
```

### Response Format
```json
{
  "status": true,
  "message": "Operation successful",
  "result": [...]
}
```

---

## 🔧 Service Ports

| Service | Port |
|---------|------|
| API Gateway | 8085 |
| User Service | 8080 |
| Patient Service | 8081 |
| Doctor Service | 8082 |
| Time Slot Service | 8082 |
| EHR Service | 8083 |
| Hospital Service | 8084 |
| Appointment Service | 8086 |

---

## 📚 Xem thêm

Chi tiết đầy đủ: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

