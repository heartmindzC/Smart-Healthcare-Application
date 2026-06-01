# Business Rules Checklist - Appointment Status Transition

> **Mục đích:** Trích xuất toàn bộ business rules hiện có trước khi apply Template Pattern

---

## 1. Business Rules về TRẠNG THÁI (Status)

### 1.1 Enum Status hiện có

| Status | Mô tả |
|--------|-------|
| `PENDING` | Chờ xác nhận |
| `CONFIRMED` | Đã xác nhận |
| `CANCELLED` | Đã hủy |
| `COMPLETED` | Đã hoàn thành |
| `NO_SHOW` | Bệnh nhân không đến |

---

## 2. Business Rules về STATUS TRANSITION

### 2.1 Các Endpoint hiện tại

| Endpoint | Method | Status mới | Controller Line |
|----------|--------|------------|-----------------|
| `/confirm/{appointmentId}` | PATCH | CONFIRMED | 173-176 |
| `/cancel/{appointmentId}` | PATCH | CANCELLED | 179-182 |
| `/complete/{appointmentId}` | PATCH | COMPLETED | 185-188 |
| `/update-status/{appointmentId}` | PATCH | Any status | 159-170 |

### 2.2 Status Transition Rules (IMPLICIT - chưa được validate)

```
PENDING    → CONFIRMED  (qua /confirm)
PENDING    → CANCELLED  (qua /cancel)
CONFIRMED  → COMPLETED  (qua /complete)
CONFIRMED  → CANCELLED  (qua /cancel)
CONFIRMED  → NO_SHOW    (qua /update-status)
COMPLETED  → (terminal state - không chuyển tiếp)
CANCELLED  → (terminal state - không chuyển tiếp)
```

### 2.3 Rules CẦN THÊM (TODO)

| # | Rule | Ưu tiên | Handler |
|---|------|---------|---------|
| 1 | COMPLETED không thể chuyển sang trạng thái khác | Cao | `CompleteAppointmentHandler` |
| 2 | CANCELLED không thể chuyển sang trạng thái khác | Cao | `CancelAppointmentHandler` |
| 3 | Chỉ PENDING mới có thể CONFIRMED | Cao | `ConfirmAppointmentHandler` |
| 4 | Chỉ CONFIRMED mới có thể COMPLETED | Cao | `CompleteAppointmentHandler` |

---

## 3. Business Rules về TIME SLOT

### 3.1 Khi CANCEL Appointment

| # | Rule | Hiện tại | Handler |
|---|------|----------|---------|
| 1 | Nếu appointment có `timeSlotId`, cần gọi Doctor Service để release time slot | TODO (chưa implement) | `CancelAppointmentHandler` |

**Code hiện tại (Controller line 164-166):**
```java
if (status == AppointmentStatus.CANCELLED && updatedAppointment.getTimeSlotId() != null) {
    // TODO: Gọi Doctor Service để mark time slot là available
}
```

### 3.2 Khi DELETE Appointment

| # | Rule | Hiện tại | Handler |
|---|------|----------|---------|
| 1 | Nếu appointment có `timeSlotId`, cần gọi Doctor Service để release time slot | TODO (chưa implement) | `DeleteAppointmentHandler` |

**Code hiện tại (Controller line 194-198):**
```java
// Nếu có time slot, mark là available trở lại
// if (appointment.getTimeSlotId() != null) {
//     // TODO: Gọi Doctor Service để mark time slot là available
// }
```

### 3.3 Khi CONFIRM Appointment

| # | Rule | Hiện tại | Handler |
|---|------|----------|---------|
| 1 | Xác nhận time slot đã được đặt thành công | Chưa implement | `ConfirmAppointmentHandler` |
| 2 | Gửi email xác nhận cho bệnh nhân | Chưa implement | `ConfirmAppointmentHandler` |

---

## 4. Business Rules về VALIDATION

### 4.1 Appointment tồn tại

| # | Rule | Xử lý |
|---|------|-------|
| 1 | Nếu `appointmentId` không tồn tại → throw `AppException(NOT_FOUND)` | Đã implement (Service line 30) |

### 4.2 Appointment Data Validation (khi tạo/cập nhật)

| # | Rule | Nơi xử lý |
|---|------|-----------|
| 1 | Validate các required fields (`@Valid`) | Controller |
| 2 | Set `createdAt` = now khi tạo mới | Service line 79 |
| 3 | Set `updatedAt` = now khi tạo/cập nhật | Service line 80, 88 |

---

## 5. Business Rules về NOTIFICATION

### 5.1 Email Notification (Đã define, chưa implement)

| # | Rule | Trigger | Status |
|---|------|---------|--------|
| 1 | Gửi email xác nhận khi appointment được CONFIRMED | `/confirm` endpoint | Chưa implement (commented) |
| 2 | Gửi email thông báo khi appointment bị CANCELLED | `/cancel` endpoint | Chưa implement |
| 3 | Gửi email reminder trước ngày hẹn | Cron job | Chưa implement |

**Code commented (Controller line 210-255):**
```java
// @PostMapping("/send-confirmation-email/{appointmentId}")
// emailService.sendAppointmentConfirmationEmail(email, appointment, hospitalAddress);
```

---

## 6. Business Rules về CROSS-SERVICE CALLS

### 6.1 Doctor Service (Time Slot Management)

| # | Operation | Khi nào | Trong Handler nào |
|---|-----------|---------|-------------------|
| 1 | Release time slot (mark available) | CANCELLED | `CancelAppointmentHandler` |
| 2 | Release time slot (mark available) | DELETE | `DeleteAppointmentHandler` |
| 3 | Confirm time slot booking | CONFIRMED | `ConfirmAppointmentHandler` |

### 6.2 Hospital Service (Optional)

| # | Operation | Khi nào | Mục đích |
|---|-----------|---------|----------|
| 1 | Lấy hospital address | Gửi email confirmation | Hiển thị địa chỉ bệnh viện |

**Code hiện tại (commented):**
```java
// String hospitalAddress = hospitalServiceClient.getHospitalAddress(appointment.getHospitalId())
//         .orElse(null);
```

---

## 7. Checklist trước khi Implement Handler

### 7.1 Xác định các Handler cần tạo

- [x] `AbstractAppointmentStatusHandler` (base class)
- [x] `ConfirmAppointmentHandler`
- [x] `CancelAppointmentHandler`
- [x] `CompleteAppointmentHandler`
- [x] `AppointmentStatusHandlerFactory`

### 7.2 Business Logic cần migrate vào từng Handler

#### ConfirmAppointmentHandler
- [x] Validate: Chỉ PENDING → CONFIRMED
- [x] Update status = CONFIRMED
- [x] **Mark time slot as booked khi confirm** (PRIORITY)
- [x] Gửi email xác nhận cho bệnh nhân (TODO - EmailService chưa implement)

#### CancelAppointmentHandler
- [x] Validate: PENDING → CANCELLED, CONFIRMED → CANCELLED
- [x] Validate: COMPLETED/CANCELLED là terminal (không thể cancel)
- [x] Update status = CANCELLED
- [x] **Gọi Doctor Service để release time slot (PRIORITY 1)**
- [ ] Gửi email cancellation notification (TODO - EmailService chưa implement)

#### CompleteAppointmentHandler
- [x] Validate: Chỉ CONFIRMED → COMPLETED
- [x] Validate: COMPLETED/CANCELLED là terminal
- [x] Update status = COMPLETED
- [ ] Cập nhật medical records (TODO)
- [ ] Gửi email completion notification (TODO - EmailService chưa implement)

### 7.3 Cross-Service Dependencies cần setup

- [x] Inject `DoctorServiceClient` vào handlers (để call release/confirm time slot)
- [ ] Inject `EmailService` vào handlers (để send notifications - đã inject nhưng chưa sử dụng)

---

## 8. Template Method Pattern - Method Skeleton

```java
public final Appointment changeStatus(String appointmentId, AppointmentStatus newStatus) {
    // 1. Validate appointment exists
    Appointment appointment = appointmentService.findById(appointmentId);
    
    // 2. Validate status transition
    validateTransition(appointment.getStatus(), newStatus);
    
    // 3. Pre-process (specific logic before status change)
    preProcess(appointmentId, newStatus);
    
    // 4. Change status
    Appointment updatedAppointment = doChangeStatus(appointmentId, newStatus);
    
    // 5. Post-process (notifications, logging, etc.)
    postProcess(updatedAppointment);
    
    // 6. Notify related services
    notifyRelatedServices(updatedAppointment);
    
    return updatedAppointment;
}
```

---

## 9. Error Handling Checklist

| # | Error Case | Xử lý |
|---|------------|-------|
| 1 | Appointment không tồn tại | Throw `AppException(NOT_FOUND)` |
| 2 | Invalid status transition | Throw `AppException(INVALID_STATUS_TRANSITION)` |
| 3 | Doctor Service unavailable | Log error + throw exception hoặc retry |
| 4 | Time slot not found | Log warning + continue (không block) |

---

## 10. Testing Checklist

### 10.1 Unit Tests cho mỗi Handler

- [ ] Test valid transitions
- [ ] Test invalid transitions (phải throw exception)
- [ ] Test preProcess logic
- [ ] Test postProcess logic
- [ ] Test cross-service calls (mocked)

### 10.2 Integration Tests

- [ ] Test end-to-end flow với database
- [ ] Test cross-service communication

---

**Ngày tạo:** 2026-03-17
**Trạng thái:** Ready để implement
