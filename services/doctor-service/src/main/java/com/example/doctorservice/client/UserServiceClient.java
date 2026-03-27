package com.example.doctorservice.client;

import com.example.common_exception.AppException;
import com.example.doctorservice.dto.request.UserCreationRequest;
import com.example.doctorservice.exception.DoctorErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${service.user.url:http://host.docker.internal:8080}")
    private String userServiceUrl;

    /**
     * Gọi API POST /users/register của user-service để tạo user mới.
     *
     * @param request thông tin user cần tạo
     * @throws AppException USER_SERVICE_UNAVAILABLE nếu không kết nối được
     * @throws AppException USER_CREATION_FAILED nếu user-service trả về lỗi (4xx/5xx)
     */
    public void createUser(UserCreationRequest request) {
        String url = userServiceUrl + "/users/register";
        log.info("Gọi user-service tạo user: url={}, userId={}", url, request.getUserId());

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("user-service trả lỗi: status={}", response.getStatusCode());
                throw new AppException(DoctorErrorCode.USER_CREATION_FAILED);
            }

            log.info("Tạo user thành công: userId={}", request.getUserId());

        } catch (HttpClientErrorException e) {
            HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
            log.error("user-service trả lỗi client: status={}, body={}", status, e.getResponseBodyAsString());

            if (status == HttpStatus.CONFLICT) {
                // User đã tồn tại (409 Conflict)
                throw new AppException(DoctorErrorCode.USER_ALREADY_EXIST);
            }
            throw new AppException(DoctorErrorCode.USER_CREATION_FAILED);

        } catch (ResourceAccessException e) {
            log.error("Không thể kết nối tới user-service: {}", e.getMessage());
            throw new AppException(DoctorErrorCode.USER_SERVICE_UNAVAILABLE);
        }
    }
}
