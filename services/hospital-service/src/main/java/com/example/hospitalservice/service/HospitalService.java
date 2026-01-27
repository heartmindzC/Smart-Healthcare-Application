package com.example.hospitalservice.service;

import com.example.hospitalservice.dto.request.HospitalCreateRequest;
import com.example.hospitalservice.dto.request.HospitalRequest;
import com.example.hospitalservice.mapper.HospitalMapper;
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
    @Autowired
    private HospitalMapper hospitalMapper;

    public List<Hospital> findAll() {
        return (List<Hospital>) hospitalRepository.findAll();
    }

    public Hospital findById(String id) {
        return hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public List<Hospital> findByHospitalName(String hospitalName) {
        return hospitalRepository.findHospitalByHospitalNameContainingIgnoreCase(hospitalName);
    }

    public List<Hospital> findByAddressContaining(String address) {
        return hospitalRepository.findHospitalByHospitalAddressContaining(address);
    }

    public Hospital update(String hospitalId, HospitalCreateRequest request) {
        Hospital hospital = findById(hospitalId);

        hospital.setHospitalName(request.getHospitalName());
        hospital.setHospitalAddress(request.getHospitalAddress());
        hospital.setHospitalPhone(request.getHospitalPhone());
        hospital.setHospitalEmail(request.getHospitalEmail());
        return hospitalRepository.save(hospital);
    }

//    public List<Hospital> searchByRequest(HospitalRequest request) {
//        List<Hospital> results = new ArrayList<>();
//
//        if (request.getHospitalId() != null) {
//            // Tìm theo ID - trả về 1 bệnh viện cụ thể
//            Optional<Hospital> hospital = hospitalRepository.findById(request.getHospitalId());
//            if (hospital.isPresent()) {
//                results.add(hospital.get());
//            }
//        } else if (request.getHospitalName() != null && !request.getHospitalName().isEmpty()) {
//            // Tìm theo tên bệnh viện - trả về list các chi nhánh (cùng tên, khác địa chỉ)
//            // Sử dụng case-insensitive và partial match để tìm kiếm linh hoạt hơn
//            results = hospitalRepository.findHospitalByHospitalNameContainingIgnoreCase(request.getHospitalName());
//        }
//
//        return results;
//    }

    public Hospital save(HospitalCreateRequest hospitalRequest) {
        Hospital hospital = hospitalMapper.toHospital(hospitalRequest);
        return hospitalRepository.save(hospital);
    }

    public void deleteById(String id) {
        hospitalRepository.deleteById(id);
    }
}

