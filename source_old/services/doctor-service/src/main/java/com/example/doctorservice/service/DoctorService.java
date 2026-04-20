package com.example.doctorservice.service;

import com.example.doctorservice.dto.DoctorRequest;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> findAll() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    public Optional<Doctor> findById(int id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> findByUserId(int userId) {
        return doctorRepository.findDoctorByUserId(userId);
    }

    public List<Doctor> findByHospitalId(int hospitalId) {
        return doctorRepository.findDoctorByHospitalId(hospitalId);
    }

    public List<Doctor> findByDepartment(String department) {
        return doctorRepository.findDoctorByDepartment(department);
    }

    public List<Doctor> findByFullName(String fullName) {
        return doctorRepository.findDoctorByFullName(fullName);
    }

    public List<Doctor> searchByHospitalIdAndDepartment(DoctorRequest request) {
        return doctorRepository.findDoctorByHospitalIdAndDepartment(
            request.getHospitalId(), 
            request.getDepartment()
        );
    }
}
