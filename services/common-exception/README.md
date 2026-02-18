#  Common Library

Thư viện lõi chứa các module dùng chung cho hệ thống Smart Healthcare Microservices.

##  Modules chính
| Module | Mô tả | Tài liệu chi tiết |
| :--- | :--- | :--- |
| **Exception Handling** | Xử lý lỗi tập trung, cơ chế ErrorCode Registry. | [Xem hướng dẫn](docs/exception-guide.md) 👈 |
| **Response Format** | Chuẩn hóa JSON trả về (`ApiResponse`). | [Xem hướng dẫn](docs/response-guide.md) |
| **Utils** | Các tiện ích Date, String, Security. | [Xem hướng dẫn](docs/utils-guide.md) |

##  Hướng dẫn cài đặt
Thêm vào `pom.xml`:
```xml
<dependency>
    <groupId>com.smarthealthcare</groupId>
    <artifactId>common-library</artifactId>
    <version>1.0.0</version>
</dependency>