# API Gateway Documentation
## Smart Healthcare System - API Reference

**Base URL**: `http://localhost:8085`  
**API Gateway Port**: `8085`

---

## 📋 Mục lục

1. [Tổng quan](#tổng-quan)
2. [Cấu hình Routes](#cấu-hình-routes)
3. [API Endpoints](#api-endpoints)
   - [Hospital Service](#hospital-service)
   - [Department Service](#department-service)
   - [User Service](#user-service)
   - [Patient Service](#patient-service)
   - [Doctor Service](#doctor-service)
   - [EHR Service](#ehr-service)
4. [Response Format](#response-format)
5. [CORS Configuration](#cors-configuration)
6. [Environment Variables](#environment-variables)

---

## Tổng quan

API Gateway là điểm truy cập duy nhất cho tất cả các microservices trong hệ thống Smart Healthcare System. Tất cả các request đều được định tuyến qua API Gateway với prefix `/api`.

### Cấu trúc URL
```
http://localhost:8085/api/{service}/{endpoint}
```

---

## Cấu hình Routes

| Service | Route ID | Gateway Path | Service URI | Service Port |
|---------|----------|--------------|-------------|--------------|
| User Service | user-service | `/api/users/**` | `http://user-service:8080` | 8080 |
| Patient Service | patient-service | `/api/patients/**` | `http://patient-service:8081` | 8081 |
| Doctor Service | doctor-service | `/api/doctors/**` | `http://doctor-service:8082` | 8082 |
| EHR Service | ehr-service | `/api/ehr/**` | `http://ehr-service:8083` | 8083 |
| Hospital Service | hospital-service | `/api/hospitals/**` | `http://hospital-service:8084` | 8084 |
| Department Service | department-service | `/api/departments/**` | `http://hospital-service:8084` | 8084 |

---

## API Endpoints

### Hospital Service

Base Path: `/api/hospitals`

#### 1. Lấy tất cả bệnh viện
```http
GET /api/hospitals/all
```

**Response:**
```json
{
  "status": true,
  "message": "Hospitals found: 3",
  "result": [
    {
      "hospitalId": 1,
      "hospitalName": "Bệnh viện Đa khoa Trung ương",
      "hospitalAddress": "123 Đường ABC, Quận 1, TP.HCM",
      "hospitalPhone": "028-1234-5678",
      "hospitalEmail": "info@hospital1.vn"
    }
  ]
}
```

#### 2. Lấy bệnh viện theo ID
```http
GET /api/hospitals/get-hospital/{hospitalId}
```

**Path Parameters:**
- `hospitalId` (int): ID của bệnh viện

**Example:**
```http
GET /api/hospitals/get-hospital/1
```

#### 3. Lấy bệnh viện theo tên
```http
GET /api/hospitals/get-hospital-by-name/{hospitalName}
```

**Path Parameters:**
- `hospitalName` (string): Tên bệnh viện

**Example:**
```http
GET /api/hospitals/get-hospital-by-name/Bệnh viện Chợ Rẫy
```

#### 4. Tìm kiếm bệnh viện theo địa chỉ
```http
GET /api/hospitals/search-by-address/{address}
```

**Path Parameters:**
- `address` (string): Địa chỉ cần tìm (partial match)

**Example:**
```http
GET /api/hospitals/search-by-address/Quận 1
```

#### 5. Tìm kiếm bệnh viện (POST)
```http
POST /api/hospitals/search
```

**Request Body:**
```json
{
  "hospitalId": 1,
  "hospitalName": "Bệnh viện",
  "hospitalAddress": "TP.HCM"
}
```

**Response:**
```json
{
  "status": true,
  "message": "Hospitals found: 2",
  "result": [...]
}
```

#### 6. Tạo bệnh viện mới
```http
POST /api/hospitals/create
```

**Request Body:**
```json
{
  "hospitalName": "Bệnh viện Mới",
  "hospitalAddress": "123 Đường XYZ",
  "hospitalPhone": "028-9999-8888",
  "hospitalEmail": "info@newhospital.vn"
}
```

**Response:**
```json
{
  "status": true,
  "message": "Hospital created successfully",
  "result": [
    {
      "hospitalId": 4,
      "hospitalName": "Bệnh viện Mới",
      ...
    }
  ]
}
```

#### 7. Cập nhật bệnh viện
```http
PUT /api/hospitals/update/{hospitalId}
```

**Path Parameters:**
- `hospitalId` (int): ID của bệnh viện cần cập nhật

**Request Body:**
```json
{
  "hospitalName": "Bệnh viện Đã Cập Nhật",
  "hospitalAddress": "456 Đường Mới",
  "hospitalPhone": "028-1111-2222",
  "hospitalEmail": "new@hospital.vn"
}
```

#### 8. Xóa bệnh viện
```http
DELETE /api/hospitals/delete/{hospitalId}
```

**Path Parameters:**
- `hospitalId` (int): ID của bệnh viện cần xóa

**Response:**
```json
{
  "status": true,
  "message": "Hospital deleted successfully",
  "result": null
}
```

---

### Department Service

Base Path: `/api/departments`

#### 1. Lấy tất cả khoa/phòng ban
```http
GET /api/departments/all
```

**Response:**
```json
{
  "status": true,
  "message": "Departments found: 7",
  "result": [
    {
      "departmentId": 1,
      "departmentName": "Khoa Nội tổng quát",
      "departmentPhone": "028-1234-5679",
      "departmentEmail": "noitongquat@hospital1.vn",
      "hospitalId": 1
    }
  ]
}
```

#### 2. Lấy khoa/phòng ban theo ID
```http
GET /api/departments/get-department/{departmentId}
```

**Path Parameters:**
- `departmentId` (int): ID của khoa/phòng ban

#### 3. Lấy khoa/phòng ban theo tên
```http
GET /api/departments/get-department-by-name/{departmentName}
```

**Path Parameters:**
- `departmentName` (string): Tên khoa/phòng ban

#### 4. Lấy khoa/phòng ban theo bệnh viện
```http
GET /api/departments/get-departments-by-hospital/{hospitalId}
```

**Path Parameters:**
- `hospitalId` (int): ID của bệnh viện

**Example:**
```http
GET /api/departments/get-departments-by-hospital/1
```

#### 5. Tìm kiếm khoa/phòng ban theo tên
```http
GET /api/departments/search-by-name/{departmentName}
```

**Path Parameters:**
- `departmentName` (string): Tên khoa/phòng ban (partial match)

#### 6. Tìm kiếm khoa/phòng ban (POST)
```http
POST /api/departments/search
```

**Request Body:**
```json
{
  "departmentId": 1,
  "departmentName": "Nội",
  "hospitalId": 1
}
```

#### 7. Tạo khoa/phòng ban mới
```http
POST /api/departments/create
```

**Request Body:**
```json
{
  "departmentName": "Khoa Mới",
  "departmentPhone": "028-9999-7777",
  "departmentEmail": "new@department.vn",
  "hospitalId": 1
}
```

#### 8. Cập nhật khoa/phòng ban
```http
PUT /api/departments/update/{departmentId}
```

**Path Parameters:**
- `departmentId` (int): ID của khoa/phòng ban

**Request Body:**
```json
{
  "departmentName": "Khoa Đã Cập Nhật",
  "departmentPhone": "028-1111-3333",
  "departmentEmail": "updated@department.vn",
  "hospitalId": 1
}
```

#### 9. Xóa khoa/phòng ban
```http
DELETE /api/departments/delete/{departmentId}
```

**Path Parameters:**
- `departmentId` (int): ID của khoa/phòng ban

---

### User Service

Base Path: `/api/users`

> **Lưu ý**: Chi tiết endpoints của User Service cần tham khảo trong User Service documentation.

**Service URI**: `http://user-service:8080`  
**Gateway Path**: `/api/users/**` → `/users/**`

---

### Patient Service

Base Path: `/api/patients`

> **Lưu ý**: Chi tiết endpoints của Patient Service cần tham khảo trong Patient Service documentation.

**Service URI**: `http://patient-service:8081`  
**Gateway Path**: `/api/patients/**` → `/patients/**`

---

### Doctor Service

Base Path: `/api/doctors`

> **Lưu ý**: Chi tiết endpoints của Doctor Service cần tham khảo trong Doctor Service documentation.

**Service URI**: `http://doctor-service:8082`  
**Gateway Path**: `/api/doctors/**` → `/doctors/**`

---

### EHR Service

Base Path: `/api/ehr`

> **Lưu ý**: Chi tiết endpoints của EHR Service cần tham khảo trong EHR Service documentation.

**Service URI**: `http://ehr-service:8083`  
**Gateway Path**: `/api/ehr/**` → `/ehr/**`

---

## Response Format

Tất cả các API endpoints trả về response theo format chuẩn:

### Success Response
```json
{
  "status": true,
  "message": "Operation successful",
  "result": [...]
}
```

### Error Response
```json
{
  "status": false,
  "message": "Error description",
  "result": null
}
```

### Response Fields
- `status` (boolean): Trạng thái của request (true = thành công, false = thất bại)
- `message` (string): Thông báo mô tả kết quả
- `result` (object/array/null): Dữ liệu trả về

---

## CORS Configuration

API Gateway được cấu hình để cho phép CORS từ mọi nguồn:

```properties
spring.cloud.gateway.globalcors.cors-configurations[/**].allowedOriginPatterns=*
spring.cloud.gateway.globalcors.cors-configurations[/**].allowedMethods=GET,POST,PUT,DELETE,OPTIONS,PATCH
spring.cloud.gateway.globalcors.cors-configurations[/**].allowedHeaders=*
spring.cloud.gateway.globalcors.cors-configurations[/**].allowCredentials=true
```

**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS, PATCH  
**Allowed Origins**: * (tất cả)  
**Allowed Headers**: * (tất cả)

---

## Environment Variables

Các biến môi trường có thể được cấu hình để override default values:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `SERVER_PORT` | `8085` | Port của API Gateway |
| `USER_SERVICE_URI` | `http://user-service:8080` | URI của User Service |
| `PATIENT_SERVICE_URI` | `http://patient-service:8081` | URI của Patient Service |
| `DOCTOR_SERVICE_URI` | `http://doctor-service:8082` | URI của Doctor Service |
| `EHR_SERVICE_URI` | `http://ehr-service:8083` | URI của EHR Service |
| `HOSPITAL_SERVICE_URI` | `http://hospital-service:8084` | URI của Hospital Service |

### Ví dụ sử dụng Environment Variables

**Docker Compose:**
```yaml
environment:
  - SERVER_PORT=8085
  - HOSPITAL_SERVICE_URI=http://hospital-service:8084
```

**Local Development:**
```bash
export HOSPITAL_SERVICE_URI=http://localhost:8084
```

---

## Ví dụ sử dụng

### cURL Examples

#### Lấy tất cả bệnh viện
```bash
curl -X GET http://localhost:8085/api/hospitals/all
```

#### Tạo bệnh viện mới
```bash
curl -X POST http://localhost:8085/api/hospitals/create \
  -H "Content-Type: application/json" \
  -d '{
    "hospitalName": "Bệnh viện Mới",
    "hospitalAddress": "123 Đường XYZ",
    "hospitalPhone": "028-9999-8888",
    "hospitalEmail": "info@newhospital.vn"
  }'
```

#### Lấy khoa/phòng ban theo bệnh viện
```bash
curl -X GET http://localhost:8085/api/departments/get-departments-by-hospital/1
```

### JavaScript/Fetch Examples

#### GET Request
```javascript
fetch('http://localhost:8085/api/hospitals/all')
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

#### POST Request
```javascript
fetch('http://localhost:8085/api/hospitals/create', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    hospitalName: 'Bệnh viện Mới',
    hospitalAddress: '123 Đường XYZ',
    hospitalPhone: '028-9999-8888',
    hospitalEmail: 'info@newhospital.vn'
  })
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));
```

---

## Troubleshooting

### Lỗi kết nối đến service
- Kiểm tra service đang chạy
- Kiểm tra URI trong `application.properties`
- Kiểm tra network/firewall settings

### Lỗi CORS
- Đảm bảo CORS configuration đã được cấu hình đúng
- Kiểm tra headers trong request

### Lỗi 404 Not Found
- Kiểm tra route path trong API Gateway
- Kiểm tra endpoint path trong service
- Kiểm tra rewrite path configuration

---

## Changelog

### Version 1.0.0
- Thêm Hospital Service routes
- Thêm Department Service routes
- Cấu hình CORS
- Tài liệu API đầy đủ

---

## Liên hệ & Hỗ trợ

Để biết thêm thông tin hoặc báo lỗi, vui lòng liên hệ team phát triển.

---

**Last Updated**: 2025-11-23

