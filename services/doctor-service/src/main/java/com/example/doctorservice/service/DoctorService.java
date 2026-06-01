package com.example.doctorservice.service;

import com.example.common_exception.AppException;
import com.example.doctorservice.client.UserServiceClient;
import com.example.doctorservice.dto.request.DoctorCreationRequest;
import com.example.doctorservice.dto.request.DoctorUpdateRequest;
import com.example.doctorservice.dto.request.UserCreationRequest;
import com.example.doctorservice.exception.DoctorErrorCode;
import com.example.doctorservice.mapper.DoctorMapper;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserServiceClient userServiceClient;

    public List<Doctor> findAll() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    public Doctor findById(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }

    public Doctor findByUserId(String userId) {
        return doctorRepository.findDoctorByUserId(userId)
                .orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }

    public List<Doctor> findByHospitalId(String hospitalId) {
        return doctorRepository.findDoctorByHospitalId(hospitalId);
    }

    public List<Doctor> findByDepartment(String department) {
        return doctorRepository.findDoctorByDepartment(department);
    }

    /**
     * Tạo bác sĩ mới theo flow:
     * 1. Kiểm tra doctor với userId đó chưa tồn tại
     * 2. Gọi user-service để tạo tài khoản user → lỗi thì throw exception
     * 3. Tạo bản ghi doctor
     */
    public Doctor save(DoctorCreationRequest request) {
        // 1. Kiểm tra doctor với userId đã tồn tại chưa
        if (doctorRepository.existsByUserId(request.getUserId())) {
            throw new AppException(DoctorErrorCode.USER_ALREADY_EXIST);
        }

        // 2. Gọi user-service tạo user (ném AppException nếu thất bại)
        UserCreationRequest userRequest = UserCreationRequest.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .phone(request.getPhone())
                .email(request.getEmail())
                .fullname(request.getFullName())
                .address(request.getAddress())
                .birth(request.getBirth())
                .gender(request.getGender())
                .roles(Set.of("DOCTOR"))
                .build();

        userServiceClient.createUser(userRequest);

        // 3. Tạo doctor sau khi user được tạo thành công
        Doctor doctor = doctorMapper.toDoctor(request);
        doctor.setRegistrationAt(new Date());
        doctor.setIsActive(true);

        log.info("Tạo doctor thành công: userId={}", request.getUserId());
        return doctorRepository.save(doctor);
    }

    public Doctor update(String id, DoctorUpdateRequest request) {
        Doctor doctor = findById(id);

        doctor.setHospitalId(request.getHospitalId());
        doctor.setDepartment(request.getDepartment());
        doctor.setFullName(request.getFullName());
        doctor.setBirth(request.getBirth());
        doctor.setGender(request.getGender());
        doctor.setLicenseId(request.getLicenseId());

        return doctorRepository.save(doctor);
    }
}
