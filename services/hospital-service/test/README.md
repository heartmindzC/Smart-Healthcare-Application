# Hospital Service API Tests (Python)

Thư mục này chứa các test API cho Hospital Service được viết bằng Python.

## Yêu cầu

- Python 3.6+
- Thư viện `requests` (sẽ được cài đặt tự động)

## Cài đặt

```bash
pip install -r test/requirements.txt
```

Hoặc:

```bash
pip install requests
```

## Cách chạy test

### Sử dụng script (Khuyến nghị)

**Linux/Mac:**
```bash
./test/run_tests.sh
```

**Windows:**
```cmd
test\run_tests.bat
```

### Chạy trực tiếp với Python

```bash
python3 test/test_api.py
```

## Cấu hình

Mặc định, test sẽ kết nối đến server tại `http://localhost:8084`.

Để thay đổi URL, sửa biến `BASE_URL` trong file `test_api.py`:

```python
BASE_URL = "http://localhost:8084"  # Thay đổi URL tại đây
```

## Kết quả test

Sau khi chạy test, kết quả sẽ được lưu vào file `result.json` ở thư mục gốc của project.

File `result.json` chứa:
- Thông tin chi tiết của từng test case (endpoint, status, response code, error nếu có)
- Summary tổng hợp (tổng số test, số test passed/failed/skipped, success rate)
- Timestamp khi chạy test

## Các API được test

### Hospital APIs:
1. **GET /hospitals/** - Lấy danh sách tất cả bệnh viện
2. **POST /hospitals/** - Tạo bệnh viện mới
3. **GET /hospitals/{hospitalId}** - Lấy thông tin bệnh viện theo ID
4. **PUT /hospitals/{hospitalId}** - Cập nhật thông tin bệnh viện
5. **DELETE /hospitals/{hospitalId}** - Xóa bệnh viện

### Department APIs:
1. **GET /departments/** - Lấy danh sách tất cả khoa
2. **POST /departments/** - Tạo khoa mới
3. **GET /departments/{departmentId}** - Lấy thông tin khoa theo ID
4. **GET /departments/hospital/{hospitalId}** - Lấy danh sách khoa theo bệnh viện
5. **PUT /departments/{departmentId}** - Cập nhật thông tin khoa
6. **DELETE /departments/{departmentId}** - Xóa khoa

## Lưu ý

- **Đảm bảo server đang chạy**: Test sẽ kiểm tra kết nối đến server trước khi chạy. Nếu server không chạy, test sẽ dừng lại và báo lỗi.
- **Thứ tự test**: Test được chạy theo thứ tự để đảm bảo dependencies giữa các test case (ví dụ: cần tạo hospital trước khi tạo department).
- **Cleanup tự động**: Test sẽ tự động xóa các dữ liệu đã tạo sau khi hoàn thành (xóa department trước, sau đó xóa hospital).

## Ví dụ kết quả

```json
{
  "timestamp": 1706457600000,
  "baseUrl": "http://localhost:8084",
  "summary": {
    "total": 11,
    "passed": 11,
    "failed": 0,
    "skipped": 0,
    "successRate": 100.0
  },
  "tests": {
    "findAllHospitals": {
      "endpoint": "GET /hospitals/",
      "status": "PASSED",
      "responseCode": 200,
      "resultCount": 0
    },
    ...
  }
}
```
