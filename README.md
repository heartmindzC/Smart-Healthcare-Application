================================================================================
                    SMART HEALTHCARE APPLICATION (SHS-DP)
                Đồ án môn học - Áp dụng Design Patterns
================================================================================

I. THÔNG TIN ĐỀ TÀI
--------------------------------------------------------------------------------
- Tên đề tài: Smart Healthcare Application - Hệ thống Y tế Thông minh
- Môn học: Mobile Development / Software Engineering
- Áp dụng: Design Patterns để tối ưu hóa cấu trúc code


II. CÔNG NGHỆ SỬ DỤNG
--------------------------------------------------------------------------------
BACKEND:
  - Java 21 + Spring Boot
  - Kiến trúc Microservices (8 services)
  - Spring Cloud Gateway (API Gateway)
  - MySQL 8.0 (Database)
  - Redis (Cache)
  - Maven (Build tool)
  - Docker & Docker Compose

FRONTEND:
  - Android (Java)
  - Retrofit 2.9.0 (Networking)
  - Min SDK: 25 / Target SDK: 36

III. KIẾN TRÚC HỆ THỐNG
--------------------------------------------------------------------------------
                            [Android Apps]
                                  │
                                  ▼
                         ┌─────────────────┐
                         │  API Gateway    │
                         │  (Port: 8085)   │
                         └────────┬────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
        ▼                         ▼                         ▼
  ┌───────────┐            ┌───────────┐            ┌───────────┐
  │User Svc   │            │Patient Svc│            │Doctor Svc │
  │(8080)     │            │(8081)     │            │(8082)     │
  └───────────┘            └───────────┘            └───────────┘
        │                         │                         │
        ▼                         ▼                         ▼
  ┌───────────┐            ┌───────────┐            ┌───────────┐
  │Hospital   │            │Appt Svc   │            │EHR Svc    │
  │Svc(8084)  │            │(8086)     │            │(8083)     │
  └───────────┘            └─────┬─────┘            └───────────┘
                                 │
                                 ▼
                         ┌───────────┐            ┌───────────┐
                         │Notify Svc │            │  MySQL    │
                         │(8088)     │            │(Port:3307)│
                         └───────────┘            └───────────┘
                                                          │
                                                 ┌─────────┴─────────┐
                                                 │      Redis        │
                                                 │   (Port: 6379)   │
                                                 └───────────────────┘

IV. HƯỚNG DẪN CÀI ĐẶT & CHẠY PROJECT
--------------------------------------------------------------------------------
PHẦN 1: CHẠY BACKEND

CÁCH 1: CHẠY VỚI DOCKER COMPOSE (Khuyến nghị)
----------------------------------------------
Yêu cầu: Docker Desktop đã được cài đặt và đang chạy

1. Mở Terminal (PowerShell hoặc Command Prompt)

2. Di chuyển đến thư mục project:
   cd "C:\Users\AD\Desktop\TDTU 2023-2024\Mobile\new\SHS - DP\Smart-Helthycare-Application"

3. Chạy Docker Compose:
   docker-compose up --build

4. Đợi cho đến khi tất cả services khởi động thành công
   (Lần đầu chạy sẽ mất khoảng 5-10 phút để build)

5. Kiểm tra trạng thái:
   docker-compose ps

6. Xem logs:
   docker-compose logs -f

7. Dừng hệ thống:
   docker-compose down

   Xóa hoàn toàn (kèm database):
   docker-compose down -v


CÁCH 2: CHẠY BACKEND RIÊNG LẺ (Development)
--------------------------------------------
Yêu cầu: JDK 21, Maven, MySQL 8.0, Redis

1. Đảm bảo MySQL và Redis đang chạy:
   docker-compose up -d mysql-db redis

2. Tạo databases và import data:
   - Tạo databases: usersdb, patientsdb, doctorsdb, hospitalsdb
   - Import file init SQL (nếu có)

3. Build và chạy từng service:
   cd services/user-service
   mvn clean package -DskipTests
   java -jar target/user-service.jar

   (Lặp lại cho các service khác trong terminal riêng)



PHẦN 2: TẠO DỮ LIỆU MẪU
Bước 1: Cài đặt thư viện hỗ trợ (nếu chưa có)
   Mở Terminal/Command Prompt và chạy lệnh:
   pip install requests

Bước 2: Kiểm tra kết nối
   Đảm bảo API Gateway đang chạy tại địa chỉ: http://localhost:8085
   (Nếu chạy Docker, hãy đảm bảo cổng 8085 đã được map chính xác).

Bước 3: Chạy Script nạp dữ liệu
   Di chuyển terminal vào thư mục source và thực hiện lệnh sau:
   python data/data.py
   hoặc python3 data/data.py



PHẦN 3: MỞ ANDROID APPS
Cách A: Dùng Android Studio (Khuyến nghị)

1. Mở Android Studio

2. Open Project > chọn thư mục:
   - ui/patient-ui   (App bệnh nhân)
   HOẶC
   - ui/doctor-ui     (App bác sĩ)

3. Sync Gradle (nếu hỏi)

4. Run App:
   - Run > Run 'app'
   - Chọn Emulator (Pixel 6, API 33 trở lên)

5. App sẽ mở trên Emulator

Cách B: Cài APK trực tiếp (Nếu đã có file APK)
1. Copy file APK vào máy tính

2. Cài trên Android Emulator:
   - Kéo thả file APK vào Emulator
   - Hoặc: adb install app-debug.apk

3. Mở app từ màn hình home của Emulator

Cách C: Cài trên điện thoại thật

1. Bật Developer Options + USB Debugging trên điện thoại

2. Cắm điện thoại vào máy tính qua USB

3. Mở Android Studio > Run app > chọn điện thoại
HOẶC cài APK:
   adb install app-debug.apk

PHẦN 3: SỬ DỤNG ỨNG DỤNG
3.1 ĐĂNG NHẬP (App Patient - Ứng dụng Bệnh nhân)
1. Mở app Patient UI
2. Đăng nhập với tài khoản test:
   
   Cách 1: Đăng nhập trực tiếp
   ─────────────────────────────
   Username: user-patient-001
   Password: [tùy bạn đặt trong init SQL]
   Cách 2: Đăng ký tài khoản mới
   ─────────────────────────────
   Ấn "Đăng ký" > Điền thông tin > Đăng ký
   
3. Sau khi đăng nhập:
   - Xem hồ sơ cá nhân
   - Tìm bác sĩ theo bệnh viện/khoa
   - Xem lịch khám bác sĩ
   - ĐẶT LỊCH HẸN
   - Xem lịch hẹn đã đặt
   - Hủy lịch hẹn nếu cần
3.2 ĐĂNG NHẬP (App Doctor - Ứng dụng Bác sĩ)
1. Mở app Doctor UI (trong terminal khác của Android Studio)
2. Đăng nhập:
   
   Username: user-doctor-001
   Password: [tùy bạn đặt trong init SQL]
3. Sau khi đăng nhập:
   - Xem lịch hẹn của mình
   - Xác nhận lịch hẹn (PENDING → CONFIRMED)
   - Xem thông tin bệnh nhân
   - Hoàn thành lịch hẹn (CONFIRMED → COMPLETED)
   - Cập nhật lịch khám (TimeSlots)
PHẦN 4: TÓM TẮT NHANH
┌─────────────────────────────────────────────────────────────────┐
│                     CÁCH CHẠY ĐẦY ĐỦ                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  BƯỚC 1: Chạy Backend                                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ Terminal 1:                                               │   │
│  │ cd "C:\...\Smart-Helthycare-Application"                │   │
│  │ docker-compose up --build                               │   │
│  │ (Đợi ~5-10 phút lần đầu)                               │   │
│  └─────────────────────────────────────────────────────────┘   │
│                          │                                      │
│                          ▼                                      │
│  BƯỚC 2: Mở App Patient                                        │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ Android Studio: Open ui/patient-ui                      │   │
│  │ Run > Run 'app' (chọn Emulator)                        │   │
│  │ Login: user-patient-001 / password                      │   │
│  │ → Đặt lịch hẹn                                         │   │
│  └─────────────────────────────────────────────────────────┘   │
│                          │                                      │
│                          ▼                                      │
│  BƯỚC 3: Mở App Doctor                                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ Android Studio: Open ui/doctor-ui (cửa sổ mới)        │   │
│  │ Run > Run 'app' (chọn Emulator)                        │   │
│  │ Login: user-doctor-001 / password                       │   │
│  │ → Xem & xác nhận lịch hẹn                              │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
PORT KẾT NỐI:
- Backend API:     http://localhost:8085
- MySQL:           localhost:3307
- Redis:           localhost:6379
APP KẾT NỐI ĐẾN:
- Android Emulator → http://10.0.2.2:8085 (tự động)
- Điện thoại thật  → http://<IP_MAYY>:8085 (cần cấu hình)
PHẦN 5: NẾU GẶP LỖI
Vấn đề	Cách xử lý
App không kết nối được backend	Kiểm tra backend đang chạy ở port 8085
Emulator không nhận localhost	Dùng IP 10.0.2.2 thay vì localhost
Muốn test trên điện thoại	Cần đổi API URL trong code sang IP máy tính
Build APK để gửi GV	Build > Build APK > Lấy file trong app/build/outputs/apk/


VI. THÔNG TIN TÀI KHOẢN TEST
-------------------------------------------------------------------------------------------------------------------
| Fullname                  | Email                          | SĐT             | Password        | Role       |
-------------------------------------------------------------------------------------------------------------------
| Trần Thị Bình             | doctor1@hospital.com           | 0912000001      | password123     | DOCTOR     |
| Lê Minh Chương            | doctor2@hospital.com           | 0912000002      | password123     | DOCTOR     |
| Phạm Anh Dương            | doctor3@hospital.com           | 0912000003      | password123     | DOCTOR     |
| Hoàng Hữu Hòa             | doctor4@hospital.com           | 0912000004      | password123     | DOCTOR     |
| Võ Quang Linh             | doctor5@hospital.com           | 0912000005      | password123     | DOCTOR     |
| Phan Văn Minh             | doctor6@hospital.com           | 0912000006      | password123     | DOCTOR     |
| Đặng Thị Nam              | doctor7@hospital.com           | 0912000007      | password123     | DOCTOR     |
| Nguyễn Minh Sơn           | doctor8@hospital.com           | 0912000008      | password123     | DOCTOR     |
| Trần Anh Tùng             | doctor9@hospital.com           | 0912000009      | password123     | DOCTOR     |
| Lê Hữu An                 | doctor10@hospital.com          | 0912000010      | password123     | DOCTOR     |
| Phạm Quang Bình           | doctor11@hospital.com          | 0912000011      | password123     | DOCTOR     |
| Hoàng Văn Chương          | doctor12@hospital.com          | 0912000012      | password123     | DOCTOR     |
| Võ Thị Dương              | doctor13@hospital.com          | 0912000013      | password123     | DOCTOR     |
| Phan Minh Hòa             | doctor14@hospital.com          | 0912000014      | password123     | DOCTOR     |
| Đặng Anh Linh             | doctor15@hospital.com          | 0912000015      | password123     | DOCTOR     |
| Nguyễn Hữu Minh           | doctor16@hospital.com          | 0912000016      | password123     | DOCTOR     |
| Trần Quang Nam            | doctor17@hospital.com          | 0912000017      | password123     | DOCTOR     |
| Lê Văn Sơn                | doctor18@hospital.com          | 0912000018      | password123     | DOCTOR     |
| Phạm Thị Tùng             | doctor19@hospital.com          | 0912000019      | password123     | DOCTOR     |
| Hoàng Minh An             | doctor20@hospital.com          | 0912000020      | password123     | DOCTOR     |
| Võ Anh Bình               | doctor21@hospital.com          | 0912000021      | password123     | DOCTOR     |
| Phan Hữu Chương           | doctor22@hospital.com          | 0912000022      | password123     | DOCTOR     |
| Đặng Quang Dương          | doctor23@hospital.com          | 0912000023      | password123     | DOCTOR     |
| Nguyễn Văn Hòa            | doctor24@hospital.com          | 0912000024      | password123     | DOCTOR     |
| Trần Thị Linh             | doctor25@hospital.com          | 0912000025      | password123     | DOCTOR     |
| Lê Minh Minh              | doctor26@hospital.com          | 0912000026      | password123     | DOCTOR     |
| Phạm Anh Nam              | doctor27@hospital.com          | 0912000027      | password123     | DOCTOR     |
| Hoàng Hữu Sơn             | doctor28@hospital.com          | 0912000028      | password123     | DOCTOR     |
| Võ Quang Tùng             | doctor29@hospital.com          | 0912000029      | password123     | DOCTOR     |
| Phan Văn An               | doctor30@hospital.com          | 0912000030      | password123     | DOCTOR     |
-------------------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------------------
| Fullname                  | Email                          | SĐT             | Password        | Role       |
-------------------------------------------------------------------------------------------------------------------
| Phạm Anh Bình             | patient1@gmail.com             | 0388000001      | password123     | PATIENT    |
| Hoàng Hữu Chương          | patient2@gmail.com             | 0388000002      | password123     | PATIENT    |
| Võ Quang Dương            | patient3@gmail.com             | 0388000003      | password123     | PATIENT    |
| Phan Văn Hòa              | patient4@gmail.com             | 0388000004      | password123     | PATIENT    |
| Đặng Thị Linh             | patient5@gmail.com             | 0388000005      | password123     | PATIENT    |
------------------------------------------------------------------------------------------------------------

VII. CÁC ENDPOINTS CHÍNH
--------------------------------------------------------------------------------
Base URL: http://localhost:8085/api/

AUTHENTICATION:
  POST /users/register    - Đăng ký tài khoản mới
  POST /users/login       - Đăng nhập

HOSPITALS & DEPARTMENTS:
  GET /hospitals/                          - Danh sách bệnh viện
  GET /hospitals/{hospitalId}              - Chi tiết bệnh viện
  GET /departments/hospital/{hospitalId}   - Danh sách khoa theo bệnh viện

DOCTORS & TIME SLOTS:
  GET /doctors/                           - Danh sách bác sĩ
  GET /doctors/{doctorId}                 - Chi tiết bác sĩ
  GET /doctors/hospital/{hospitalId}      - Bác sĩ theo bệnh viện
  GET /doctors/department/{deptId}        - Bác sĩ theo khoa
  GET /time-slots/doctor/{doctorId}/available  - Lịch khám trống

APPOINTMENTS:
  GET /appointments/                      - Tất cả lịch hẹn
  GET /appointments/patient/{patientId}  - Lịch hẹn của bệnh nhân
  GET /appointments/doctor/{doctorId}    - Lịch hẹn của bác sĩ
  POST /appointments/                     - Tạo lịch hẹn mới
  PATCH /appointments/confirm/{id}       - Xác nhận lịch hẹn
  PATCH /appointments/cancel/{id}        - Hủy lịch hẹn
  PATCH /appointments/complete/{id}      - Hoàn thành lịch hẹn

PATIENTS:
  GET /patients/                          - Danh sách bệnh nhân
  GET /patients/{patientId}               - Chi tiết bệnh nhân



XI. DATABASE SCHEMA
--------------------------------------------------------------------------------
usersdb:
  - users (userId, password, phone, fullname, address, birth, email, gender, role)

patientsdb:
  - patients (patientId, fullName, userId, birth, gender, insuranceId, 
              emergencyCallingNumber, job, bloodType, heights, weights)

doctorsdb:
  - doctors (doctorId, userId, hospitalId, department, fullName, birth, gender, licenseId)
  - time_slots (timeSlotId, doctorId, dayOfWeek, startTime, endTime, isAvailable)

hospitalsdb:
  - hospitals (hospitalId, hospitalName, hospitalAddress, hospitalPhone, hospitalEmail)
  - departments (departmentId, departmentName, departmentPhone, hospitalId)

appointmentsdb:
  - appointments (appointmentId, doctorId, patientId, hospitalId, departmentId,
                  timeSlotId, appointmentDateTime, status, notes, reason)

ehrdb:
  - medical_visits (visitId, patientId, doctorId, visitDate, hospital, department, diagnosis)
  - prescriptions (prescriptionId, visitId, patientId, medicationName, dosage, frequency, quantity)

XII. TROUBLESHOOTING
--------------------------------------------------------------------------------
1. Lỗi "Port already in use":
   - Kiểm tra port: netstat -ano | findstr :8080
   - Kill process: taskkill /PID <process_id> /F
   - Hoặc dùng Docker thay vì chạy local

2. Lỗi kết nối MySQL:
   - Kiểm tra MySQL container: docker-compose logs mysql-db
   - Restart: docker-compose restart mysql-db

3. Android không kết nối được backend:
   - Đảm bảo chạy trên Android Emulator (dùng 10.0.2.2)
   - Kiểm tra firewall cho phép kết nối
   - Backend phải chạy trên localhost:8085

4. Build Docker thất bại:
   - Kiểm tra Docker Desktop đang chạy
   - Xóa cache: docker-compose build --no-cache
