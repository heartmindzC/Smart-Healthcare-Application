# 📖 Hướng Dẫn Sử Dụng Common Exception Module

Tài liệu này hướng dẫn cách import và sử dụng module `common-exception` trong các microservice của hệ thống Smart Healthcare.

## 📦 1. Thêm Dependency vào Project

### Maven

Thêm dependency vào file `pom.xml` của service của bạn:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>common-exception</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**Lưu ý:** Nếu module này chưa được publish lên Maven repository, bạn cần:
1. Build module này trước: `mvn clean install`
2. Hoặc thêm dependency từ local repository
3. Hoặc sử dụng multi-module Maven project

### Gradle

Nếu bạn sử dụng Gradle, thêm vào `build.gradle`:

```gradle
dependencies {
    implementation 'com.example:common-exception:0.0.1-SNAPSHOT'
}
```

## 🎯 2. Tạo Enum ErrorCode cho Service

Mỗi service cần tạo một Enum riêng implement interface `ErrorCode`. Enum này định nghĩa tất cả các mã lỗi mà service có thể trả về.

### Ví dụ: UserServiceErrorCode

```java
package com.example.userservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum UserServiceErrorCode implements ErrorCode {
    // Lỗi chung
    UNDEFINED_ERROR(9999, "Undefined Error", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Lỗi validation
    PHONE_INVALID(1001, "Phone Number Invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1002, "Email Address Invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password Invalid", HttpStatus.BAD_REQUEST),
    
    // Lỗi không tìm thấy
    NOT_FOUND(1003, "Not Found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1003, "User Not Found", HttpStatus.NOT_FOUND),
    
    // Lỗi trùng lặp
    EMAIL_EXISTS(1019, "Email Exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1021, "Phone Exists", HttpStatus.BAD_REQUEST),
    
    // Lỗi null/empty
    EMPTY_NAME(1007, "Name Is Not Empty", HttpStatus.BAD_REQUEST),
    NULL_NAME(1008, "Name Is Null", HttpStatus.BAD_REQUEST),
    EMPTY_PASSWORD(1009, "Password Is Empty", HttpStatus.BAD_REQUEST),
    
    // Lỗi đăng nhập
    LOGIN_FAILED(1011, "Username or password is incorrect", HttpStatus.BAD_REQUEST),
    LOGIN_METHOD_INVALID(1005, "Login Method Invalid", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode status;

    UserServiceErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatusCode getStatus() {
        return status;
    }
}
```

### Cấu trúc Enum ErrorCode

Mỗi enum constant cần có:
- **code**: Mã số lỗi (int) - nên đặt theo range cho từng service
- **message**: Thông báo lỗi (String)
- **status**: HTTP status code (HttpStatusCode)

## 🔧 3. Đăng Ký ErrorCode vào Registry

Sau khi tạo Enum ErrorCode, bạn cần đăng ký nó vào `ErrorCodeRegistry` khi ứng dụng khởi động.

### Cách 1: Sử dụng @PostConstruct trong Configuration Class

```java
package com.example.userservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.userservice.exception.UserServiceErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {
    
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;
    
    @PostConstruct
    public void registerErrorCodes() {
        errorCodeRegistry.register(UserServiceErrorCode.class);
    }
}
```

### Cách 2: Sử dụng ApplicationRunner

```java
package com.example.userservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.userservice.exception.UserServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {
    
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;
    
    @Bean
    public ApplicationRunner errorCodeRegistrationRunner() {
        return args -> {
            errorCodeRegistry.register(UserServiceErrorCode.class);
        };
    }
}
```

### Cách 3: Đăng ký trong @Component

```java
package com.example.userservice.component;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.userservice.exception.UserServiceErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeInitializer {
    
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;
    
    @PostConstruct
    public void init() {
        errorCodeRegistry.register(UserServiceErrorCode.class);
    }
}
```

**Lưu ý:** Chỉ cần đăng ký một lần khi ứng dụng khởi động. Nếu có nhiều Enum ErrorCode, đăng ký tất cả trong cùng một nơi.

## 🚨 4. Sử Dụng AppException

Sau khi đã đăng ký ErrorCode, bạn có thể sử dụng `AppException` để throw exception trong code.

### Ví dụ trong Service Layer

```java
package com.example.userservice.service;

import com.example.common_exception.AppException;
import com.example.userservice.exception.UserServiceErrorCode;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findById(Long id) {
        if (id == null) {
            throw new AppException(UserServiceErrorCode.ID_NULL);
        }
        
        return userRepository.findById(id)
            .orElseThrow(() -> new AppException(UserServiceErrorCode.USER_NOT_FOUND));
    }
    
    public User createUser(User user) {
        // Validate email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(UserServiceErrorCode.EMAIL_EXISTS);
        }
        
        // Validate phone
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new AppException(UserServiceErrorCode.PHONE_EXISTS);
        }
        
        return userRepository.save(user);
    }
    
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new AppException(UserServiceErrorCode.LOGIN_FAILED));
        
        if (!user.getPassword().equals(password)) {
            throw new AppException(UserServiceErrorCode.LOGIN_FAILED);
        }
        
        return user;
    }
}
```

### Ví dụ trong Controller Layer

```java
package com.example.userservice.controller;

import com.example.common_exception.AppException;
import com.example.userservice.exception.UserServiceErrorCode;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
```

**Lưu ý:** `GlobalExceptionHandler` sẽ tự động bắt `AppException` và trả về `ApiResponse` với format chuẩn.

## ✅ 5. Sử Dụng với Validation (Bean Validation)

Module này hỗ trợ tích hợp với Spring Bean Validation. Bạn có thể sử dụng ErrorCode enum name trong validation message.

### Ví dụ: DTO với Validation

```java
package com.example.userservice.dto;

import com.example.userservice.exception.UserServiceErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    
    @NotBlank(message = UserServiceErrorCode.NULL_NAME.name())
    private String name;
    
    @Email(message = UserServiceErrorCode.EMAIL_INVALID.name())
    @NotBlank(message = UserServiceErrorCode.EMAIL_INVALID.name())
    private String email;
    
    @NotBlank(message = UserServiceErrorCode.EMPTY_PASSWORD.name())
    @Size(min = 8, message = UserServiceErrorCode.PASSWORD_INVALID.name())
    private String password;
    
    @NotBlank(message = UserServiceErrorCode.PHONE_INVALID.name())
    private String phone;
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
```

### Controller với @Valid

```java
package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        // Convert DTO to Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
```

**Cách hoạt động:**
1. Khi validation fail, Spring sẽ throw `MethodArgumentNotValidException`
2. `GlobalExceptionHandler` sẽ bắt exception này
3. Lấy enum name từ `fieldError.getDefaultMessage()`
4. Tra cứu ErrorCode từ Registry
5. Trả về `ApiResponse` với code và message tương ứng

## 📋 6. Response Format

Khi exception được xử lý, response sẽ có format:

```json
{
    "code": 1001,
    "message": "Phone Number Invalid"
}
```

HTTP Status Code sẽ được set theo `ErrorCode.getStatus()`.

## 🔍 7. Ví Dụ Hoàn Chỉnh

### Project Structure

```
userservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── userservice/
│   │   │               ├── config/
│   │   │               │   └── ExceptionConfig.java
│   │   │               ├── controller/
│   │   │               │   └── UserController.java
│   │   │               ├── dto/
│   │   │               │   └── CreateUserRequest.java
│   │   │               ├── exception/
│   │   │               │   └── UserServiceErrorCode.java
│   │   │               ├── model/
│   │   │               │   └── User.java
│   │   │               ├── repository/
│   │   │               │   └── UserRepository.java
│   │   │               └── service/
│   │   │                   └── UserService.java
│   │   └── resources/
│   │       └── application.properties
│   └── pom.xml
```

### ExceptionConfig.java

```java
package com.example.userservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.userservice.exception.UserServiceErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfig {
    
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;
    
    @PostConstruct
    public void registerErrorCodes() {
        errorCodeRegistry.register(UserServiceErrorCode.class);
    }
}
```

### UserServiceErrorCode.java

```java
package com.example.userservice.exception;

import com.example.common_exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum UserServiceErrorCode implements ErrorCode {
    USER_NOT_FOUND(1003, "User Not Found", HttpStatus.NOT_FOUND),
    EMAIL_EXISTS(1019, "Email Exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1021, "Phone Exists", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(1011, "Username or password is incorrect", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1002, "Email Address Invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password Invalid", HttpStatus.BAD_REQUEST),
    NULL_NAME(1008, "Name Is Null", HttpStatus.BAD_REQUEST),
    EMPTY_PASSWORD(1009, "Password Is Empty", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1001, "Phone Number Invalid", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode status;

    UserServiceErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatusCode getStatus() {
        return status;
    }
}
```

### UserService.java

```java
package com.example.userservice.service;

import com.example.common_exception.AppException;
import com.example.userservice.exception.UserServiceErrorCode;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new AppException(UserServiceErrorCode.USER_NOT_FOUND));
    }
    
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(UserServiceErrorCode.EMAIL_EXISTS);
        }
        
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new AppException(UserServiceErrorCode.PHONE_EXISTS);
        }
        
        return userRepository.save(user);
    }
}
```

## ⚠️ 8. Lưu Ý Quan Trọng

1. **Đăng ký ErrorCode trước khi sử dụng**: Phải đăng ký Enum ErrorCode vào Registry trước khi service xử lý request đầu tiên.

2. **Đặt tên Enum constant rõ ràng**: Tên enum constant sẽ được dùng làm key trong Registry, nên đặt tên mô tả và không trùng lặp.

3. **Quản lý Code range**: Mỗi service nên có một range code riêng để tránh trùng lặp:
   - User Service: 1000-1999
   - Doctor Service: 2000-2999
   - Appointment Service: 3000-3999
   - ...

4. **HTTP Status Code phù hợp**: Chọn HTTP status code phù hợp với từng loại lỗi:
   - `BAD_REQUEST` (400): Lỗi validation, dữ liệu không hợp lệ
   - `UNAUTHORIZED` (401): Chưa xác thực
   - `FORBIDDEN` (403): Không có quyền
   - `NOT_FOUND` (404): Không tìm thấy resource
   - `CONFLICT` (409): Xung đột (ví dụ: email đã tồn tại)
   - `INTERNAL_SERVER_ERROR` (500): Lỗi server

5. **Exception không được xử lý**: Nếu có exception không phải `AppException` hoặc `MethodArgumentNotValidException`, sẽ trả về:
   ```json
   {
       "code": 9999,
       "message": "Undefined Error"
   }
   ```
   Với HTTP status 500.

## 🎓 9. Best Practices

1. **Tạo ErrorCode enum riêng cho mỗi service**: Không chia sẻ ErrorCode enum giữa các service.

2. **Sử dụng validation annotation**: Tận dụng Bean Validation để giảm code validation thủ công.

3. **Message rõ ràng**: Viết message lỗi rõ ràng, dễ hiểu cho người dùng cuối.

4. **Logging**: Module đã tự động log exception (e.printStackTrace()), nhưng bạn có thể thêm logging chi tiết hơn trong service layer.

5. **Testing**: Test các trường hợp exception để đảm bảo ErrorCode được trả về đúng.

## 📚 10. Tài Liệu Tham Khảo

- [Exception Guide](exception-guide.md) - Tài liệu chi tiết về kiến trúc exception handling
- [Spring Boot Validation](https://spring.io/guides/gs/validating-form-input/)
- [Spring Boot Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)

---

**Chúc bạn sử dụng module thành công!** 🎉
