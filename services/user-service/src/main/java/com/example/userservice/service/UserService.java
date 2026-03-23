package com.example.userservice.service;

import com.example.common_exception.AppException;
import com.example.userservice.dto.request.*;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.exception.UserErrorCode;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j // 1. Tự động sinh ra biến log chuẩn của SLF4J (hỗ trợ dấu {})
@Service
@RequiredArgsConstructor
public class UserService {

    // 2. Chuẩn hóa Injection: Dùng private final hết, bỏ @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;
    private final List<LoginMethod> loginMethods;
    private final StringRedisTemplate redisTemplate;

    private final String NOTIFICATION_SERVICE_URL = "http://host.docker.internal:8087/notifications/";

    public UserResponse findByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> findAllUsers() {
        // 3. Rút gọn vòng lặp bằng Stream API
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsById(registerRequest.getUserId())) {
            throw new AppException(UserErrorCode.ID_EXISTS);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(UserErrorCode.EMAIL_EXISTS);
        }
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new AppException(UserErrorCode.PHONE_EXISTS);
        }

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.PATIENT);
        user.setRoles(roles);

        UserResponse response = userMapper.toUserResponse(userRepository.save(user));

        // 4. Đã sửa lỗi truyền getPhone() 2 lần thành getUserId()
        sendWelcomeEmail(response.getEmail(), response.getFullname(), response.getPhone(), response.getUserId());

        return response;
    }

    public UserResponse editUser(String userId, UserEdittingRequest request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.NOT_FOUND));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(UserErrorCode.EMAIL_EXISTS);
        }

        if (!user.getPhone().equals(request.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
            throw new AppException(UserErrorCode.PHONE_EXISTS);
        }

        user.setFullname(request.getFullname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setBirth(request.getBirth());
        user.setGender(request.getGender());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    private void sendWelcomeEmail(String email, String fullName, String phone, String userId) {
        try {
            NotificationRequest requestDto = new NotificationRequest(email, fullName, phone, userId);
            String url = NOTIFICATION_SERVICE_URL + "welcome";
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    requestDto,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Đã yêu cầu Notification Service gửi email thành công cho: {}", email);
            } else {
                log.warn("Notification Service phản hồi mã lỗi: {}", response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Không thể kết nối tới Notification Service: {}", e.getMessage());
        }
    }

    public UserResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findByUserId(updatePasswordRequest.getUserId())
                .orElseThrow(() -> new AppException(UserErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(UserErrorCode.PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse login(LoginRequest loginRequest) {
        LoginMethod loginMethod = loginMethods.stream()
                .filter(s -> s.checkType(loginRequest.getType()))
                .findFirst()
                .orElseThrow(() -> new AppException(UserErrorCode.LOGIN_METHOD_INVALID));

        User user = loginMethod.login(loginRequest.getUsername(), loginRequest.getPassword());

        return userMapper.toUserResponse(user);
    }

    // Forgot password
    // Tao ma otp ngau nhien
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public boolean processForgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(UserErrorCode.NOT_FOUND));

        String otp = generateOTP();
        String otpKey = "OTP_FORGOT_PW:" + user.getEmail();

        redisTemplate.opsForValue().set(otpKey, otp, 5, TimeUnit.MINUTES);

        return sendOtpEmail(user.getEmail(), user.getFullname(), otp);
    }

    private boolean sendOtpEmail(String email, String fullName, String otp) {
        try {
            SendOTPRequest request = new SendOTPRequest();
            request.setEmail(email);
            request.setFullName(fullName);
            request.setOtp(otp);
            String url = NOTIFICATION_SERVICE_URL + "otp";
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Đã yêu cầu Notification Service gửi email thành công cho: {}", email);
                return true;
            } else {
                log.warn("Notification Service phản hồi mã lỗi: {}", response.getStatusCode());
                throw new AppException(UserErrorCode.SEND_OTP_FAILED);
            }

        } catch (Exception e) {
            log.error("Không thể kết nối tới Notification Service: {}", e.getMessage());
            throw new AppException(UserErrorCode.CONNECTION_REFUSE);
        }
    }

    public boolean verifyOtp(VerifyOtpRequest request) {
        String otpKey = "OTP_FORGOT_PW:" + request.getEmail();
        String cachedOtp = redisTemplate.opsForValue().get(otpKey);

        if (cachedOtp == null) {
            throw new AppException(UserErrorCode.OTP_EXPIRED);
        }

        if (!cachedOtp.equals(request.getOtp())) {
            throw new AppException(UserErrorCode.OTP_INVALID);
        }

        // Ma dung thi xoa ma khoi redis
        redisTemplate.delete(otpKey);

        // tao mot bien cho phep sua trong khoang 5p
        String verifiedKey = "OTP_VERIFIED:" + request.getEmail();
        redisTemplate.opsForValue().set(verifiedKey, "true", 5, TimeUnit.MINUTES);
        return true;
    }

    public boolean resetPassword(ResetPasswordRequest request) {
        String verifiedKey = "OTP_VERIFIED:" + request.getEmail();

        String isVerified = redisTemplate.opsForValue().get(verifiedKey);

        if (isVerified == null || !isVerified.equals("true")) {
            throw new AppException(UserErrorCode.UNAUTHORIZED_RESET);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(UserErrorCode.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        redisTemplate.delete(verifiedKey);
        return true;
    }
}