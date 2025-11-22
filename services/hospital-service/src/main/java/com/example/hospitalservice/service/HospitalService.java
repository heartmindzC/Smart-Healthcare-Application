package com.example.hospitalservice.service;

import com.example.hospitalservice.dto.HospitalRequest;
import com.example.hospitalservice.model.Hospital;
import com.example.hospitalservice.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> findAll() {
        return (List<Hospital>) hospitalRepository.findAll();
    }

    public Optional<Hospital> findById(int id) {
        return hospitalRepository.findById(id);
    }

    public List<Hospital> findByHospitalName(String hospitalName) {
        // Tìm theo tên bệnh viện (case-insensitive, partial match)
        // Trả về tất cả các chi nhánh có cùng tên nhưng địa chỉ khác nhau
        return hospitalRepository.findHospitalByHospitalNameContainingIgnoreCase(hospitalName);
    }

    public List<Hospital> findByAddressContaining(String address) {
        return hospitalRepository.findHospitalByHospitalAddressContaining(address);
    }

    public List<Hospital> searchByRequest(HospitalRequest request) {
        List<Hospital> results = new ArrayList<>();
        
        if (request.getHospitalId() != null) {
            // Tìm theo ID - trả về 1 bệnh viện cụ thể
            Optional<Hospital> hospital = hospitalRepository.findById(request.getHospitalId());
            if (hospital.isPresent()) {
                results.add(hospital.get());
            }
        } else if (request.getHospitalName() != null && !request.getHospitalName().isEmpty()) {
            // Tìm theo tên bệnh viện - trả về list các chi nhánh (cùng tên, khác địa chỉ)
            // Sử dụng case-insensitive và partial match để tìm kiếm linh hoạt hơn
            results = hospitalRepository.findHospitalByHospitalNameContainingIgnoreCase(request.getHospitalName());
        }
        
        return results;
    }

    public Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public void deleteById(int id) {
        hospitalRepository.deleteById(id);
    }
}

