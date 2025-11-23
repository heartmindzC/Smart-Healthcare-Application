# Changelog - Time Slots & Appointments Integration

## 📅 Date: November 23, 2025

### ✅ Changes Made

#### 1. API Gateway Routes Configuration
**File**: `src/main/resources/application.properties`

Added 2 new routes:

##### Time Slot Service (Route #6)
```properties
spring.cloud.gateway.routes[6].id=time-slot-service
spring.cloud.gateway.routes[6].uri=${DOCTOR_SERVICE_URI:http://doctor-service:8082}
spring.cloud.gateway.routes[6].predicates[0]=Path=/api/time-slots/**
spring.cloud.gateway.routes[6].filters[0]=RewritePath=/api/time-slots/(?<segment>.*),/time-slots/$\{segment}
```

- **Gateway Path**: `/api/time-slots/**`
- **Service URI**: `http://doctor-service:8082`
- **Rewrite**: `/api/time-slots/*` → `/time-slots/*`

##### Appointment Service (Route #7)
```properties
spring.cloud.gateway.routes[7].id=appointment-service
spring.cloud.gateway.routes[7].uri=${APPOINTMENT_SERVICE_URI:http://appointment-service:8086}
spring.cloud.gateway.routes[7].predicates[0]=Path=/api/appointments/**
spring.cloud.gateway.routes[7].filters[0]=RewritePath=/api/appointments/(?<segment>.*),/appointments/$\{segment}
```

- **Gateway Path**: `/api/appointments/**`
- **Service URI**: `http://appointment-service:8086`
- **Rewrite**: `/api/appointments/*` → `/appointments/*`

---

#### 2. API Documentation
**File**: `API_DOCUMENTATION.md`

##### Updated Sections:

1. **Table of Contents**
   - Added: Time Slot Service
   - Added: Appointment Service

2. **Routes Configuration Table**
   - Added Time Slot Service row
   - Added Appointment Service row

3. **API Endpoints - New Sections**

**Time Slot Service** (8 endpoints):
- GET `/api/time-slots/all` - Lấy tất cả time slots
- GET `/api/time-slots/doctor/{doctorId}` - Lấy time slots của bác sĩ
- GET `/api/time-slots/doctor/{doctorId}/available` - Lấy slots trống
- GET `/api/time-slots/doctor/{doctorId}/available-by-date?date={date}` - Lấy theo ngày
- POST `/api/time-slots/create` - Tạo time slot
- PUT `/api/time-slots/update/{id}` - Cập nhật time slot
- PATCH `/api/time-slots/update-availability/{id}?isAvailable={bool}` - Update availability
- DELETE `/api/time-slots/delete/{id}` - Xóa time slot

**Appointment Service** (16 endpoints):
- GET `/api/appointments/all` - Lấy tất cả
- GET `/api/appointments/{id}` - Lấy theo ID
- GET `/api/appointments/patient/{patientId}` - Lấy theo bệnh nhân
- GET `/api/appointments/doctor/{doctorId}` - Lấy theo bác sĩ
- GET `/api/appointments/hospital/{hospitalId}` - Lấy theo bệnh viện
- GET `/api/appointments/status/{status}` - Lấy theo status
- GET `/api/appointments/patient/{patientId}/status/{status}` - Combo filter
- GET `/api/appointments/doctor/{doctorId}/status/{status}` - Combo filter
- GET `/api/appointments/date-range?start={start}&end={end}` - Theo thời gian
- POST `/api/appointments/create` - Tạo appointment
- PUT `/api/appointments/update/{id}` - Cập nhật
- PATCH `/api/appointments/update-status/{id}?status={status}` - Update status
- PATCH `/api/appointments/confirm/{id}` - Xác nhận
- PATCH `/api/appointments/cancel/{id}` - Hủy
- PATCH `/api/appointments/complete/{id}` - Hoàn thành
- DELETE `/api/appointments/delete/{id}` - Xóa

---

#### 3. Quick Reference Guide
**File**: `API_QUICK_REFERENCE.md`

##### Added:
- Time Slot Service section với 8 endpoints
- Appointment Service section với 16 endpoints
- Updated Service Ports table

---

### 📊 Summary

| Item | Before | After |
|------|--------|-------|
| Total Routes | 6 | **8** (+2) |
| Services Integrated | 6 | **8** (+2) |
| Documented Endpoints | ~50 | **~74** (+24) |

---

### 🌐 Access URLs

#### Via API Gateway (Recommended)

**Time Slots:**
```
http://localhost:8085/api/time-slots/all
http://localhost:8085/api/time-slots/doctor/1/available
http://localhost:8085/api/time-slots/doctor/1/available-by-date?date=2024-11-25
```

**Appointments:**
```
http://localhost:8085/api/appointments/all
http://localhost:8085/api/appointments/patient/101
http://localhost:8085/api/appointments/doctor/1
http://localhost:8085/api/appointments/status/CONFIRMED
```

#### Direct Access (Alternative)

**Time Slots (via Doctor Service):**
```
http://localhost:8082/time-slots/all
```

**Appointments (Direct):**
```
http://localhost:8086/appointments/all
```

---

### 🧪 Testing

#### Test Time Slots API
```bash
# Via API Gateway
curl http://localhost:8085/api/time-slots/all

# Get available slots for doctor 1
curl http://localhost:8085/api/time-slots/doctor/1/available

# Get available by date
curl "http://localhost:8085/api/time-slots/doctor/1/available-by-date?date=2024-11-25"
```

#### Test Appointments API
```bash
# Via API Gateway
curl http://localhost:8085/api/appointments/all

# Get by patient
curl http://localhost:8085/api/appointments/patient/101

# Get by status
curl http://localhost:8085/api/appointments/status/PENDING

# Create appointment
curl -X POST http://localhost:8085/api/appointments/create \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": 1,
    "doctorName": "Dr. Nguyen Van A",
    "patientId": 101,
    "patientName": "Nguyen Thi Lan",
    "hospitalId": 1,
    "hospitalName": "Bach Mai Hospital",
    "timeSlotId": 1,
    "appointmentDateTime": "2024-11-25T08:00:00",
    "reason": "Khám sức khỏe",
    "notes": "Khám định kỳ"
  }'
```

---

### 🔧 Environment Variables

Add these to your environment configuration if using custom URIs:

```bash
# Time Slots (uses Doctor Service)
DOCTOR_SERVICE_URI=http://doctor-service:8082

# Appointments
APPOINTMENT_SERVICE_URI=http://appointment-service:8086
```

---

### 📝 Notes

1. **Time Slot Service** is part of Doctor Service (port 8082)
2. **Appointment Service** runs independently on port 8086
3. All requests through API Gateway are prefixed with `/api`
4. Direct service access removes the `/api` prefix
5. CORS is enabled for all origins in development

---

### 🎯 Next Steps

1. ✅ Routes configured
2. ✅ Documentation updated
3. ⏳ Test all endpoints
4. ⏳ Update Postman collection
5. ⏳ Add authentication/authorization
6. ⏳ Implement rate limiting

---

### 📚 Related Documentation

- [API Documentation](./API_DOCUMENTATION.md) - Full API reference
- [Quick Reference](./API_QUICK_REFERENCE.md) - Quick lookup guide
- [Time Slots Guide](../APPOINTMENT_TIMESLOT_GUIDE.md) - Time Slots & Appointments usage
- [Database Setup](../DATABASE_SETUP_GUIDE.md) - Database initialization

---

**Updated by**: AI Assistant  
**Date**: November 23, 2025  
**Version**: 1.0.0

