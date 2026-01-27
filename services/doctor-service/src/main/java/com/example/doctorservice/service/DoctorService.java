package com.example.doctorservice.service;

import com.example.common_exception.AppException;
import com.example.doctorservice.dto.request.DoctorCreationRequest;
import com.example.doctorservice.dto.request.DoctorUpdateRequest;
import com.example.doctorservice.exception.DoctorErrorCode;
import com.example.doctorservice.mapper.DoctorMapper;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper doctorMapper;

    public List<Doctor> findAll() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    public Doctor findById(String id) {
        return doctorRepository.findById(id).orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }

    public Doctor findByUserId(String userId) {
        return doctorRepository.findDoctorByUserId(userId).orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }

    public List<Doctor> findByHospitalId(String hospitalId) {
        return doctorRepository.findDoctorByHospitalId(hospitalId);
    }

    public List<Doctor> findByDepartment(String department) {
        return doctorRepository.findDoctorByDepartment(department);
    }

    public Doctor save (DoctorCreationRequest request) {
        if (doctorRepository.existsByUserId(request.getUserId())) {
            throw new AppException(DoctorErrorCode.USER_ALREADY_EXIST);
        }

        Doctor doctor = doctorMapper.toDoctor(request);
        doctor.setRegistrationAt(new Date());
        doctor.setIsActive(true);
        return doctorRepository.save(doctor);
    }

    public Doctor update (String id, DoctorUpdateRequest request) {
        Doctor doctor = findById(id);

        doctor.setHospitalId(request.getHospitalId());
        doctor.setDepartment(request.getDepartment());
        doctor.setFullName(request.getFullName());
        doctor.setBirth(request.getBirth());
        doctor.setGender(request.getGender());
        doctor.setLicenseId(request.getLicenseId());

        return doctorRepository.save(doctor);
    }

//    public List<Doctor> findByFullName(String fullName) {
//        return doctorRepository.findDoctorByFullName(fullName);
//    }

//    public List<Doctor> searchByHospitalIdAndDepartment(DoctorRequest request) {
//        return doctorRepository.findDoctorByHospitalIdAndDepartment(
//            request.getHospitalId(),
//            request.getDepartment()
//        );
//    }
}
