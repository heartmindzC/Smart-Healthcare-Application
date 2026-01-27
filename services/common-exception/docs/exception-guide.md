# 🛡️ Exception Handling & Error Code Registry

Module này quản lý việc xử lý lỗi tập trung cho toàn bộ hệ thống Microservices dựa trên kiến trúc **Interface-based Enum** và **Registry Pattern**.

## 🧠 Nguyên lý hoạt động
Thay vì fix cứng Enum lỗi trong thư viện chung, mỗi Service sẽ tự định nghĩa Enum của riêng mình và "đăng ký" vào hệ thống khi khởi động.

```mermaid
graph LR
    A[User Service] -- 1. Register Enum --> B(ErrorCode Registry)
    C[Doctor Service] -- 1. Register Enum --> B
    D[Global Exception Handler] -- 2. Lookup Key --> B
    B -- 3. Return Error Object --> D