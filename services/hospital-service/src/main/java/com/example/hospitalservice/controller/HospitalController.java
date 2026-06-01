package com.example.hospitalservice.controller;

import com.example.hospitalservice.dto.request.HospitalCreateRequest;
import com.example.hospitalservice.dto.request.HospitalRequest;
import com.example.hospitalservice.dto.response.ApiResponse;
import com.example.hospitalservice.dto.response.HospitalResponse;
import com.example.hospitalservice.model.Hospital;
import com.example.hospitalservice.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/")
    public ApiResponse<List<Hospital>> findAll() {
        List<Hospital> hospitals = hospitalService.findAll();
        return ApiResponse.<List<Hospital>>builder()
                .result(hospitals)
                .build();
    }

    @GetMapping("/{hospitalId}")
    public ApiResponse<Hospital> findHospitalById(@PathVariable String hospitalId) {
        Hospital response = hospitalService.findById(hospitalId);
        return ApiResponse.<Hospital>builder()
                .result(response)
                .build();
    }

//    @GetMapping("/get-hospital-by-name/{hospitalName}")
//    public ResponseEntity<HospitalResponse> findHospitalByName(@PathVariable String hospitalName) {
//        HospitalResponse response = new HospitalResponse();
//        try {
//            // Tìm theo tên bệnh viện - trả về list các chi nhánh (cùng tên, khác địa chỉ)
//            List<Hospital> hospitals = hospitalService.findByHospitalName(hospitalName);
//            if (hospitals != null && !hospitals.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Found " + hospitals.size() + " hospital branch(es) with name: " + hospitalName);
//                response.setResult(hospitals);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No hospitals found with name: " + hospitalName);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error finding hospitals by name: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to retrieve hospitals: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }

//    @GetMapping("/search-by-address/{address}")
//    public ResponseEntity<HospitalResponse> searchHospitalsByAddress(@PathVariable String address) {
//        HospitalResponse response = new HospitalResponse();
//        try {
//            List<Hospital> hospitals = hospitalService.findByAddressContaining(address);
//            if (hospitals != null && !hospitals.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Hospitals found: " + hospitals.size());
//                response.setResult(hospitals);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No hospitals found with address containing: " + address);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error searching hospitals by address: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to search hospitals: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity<HospitalResponse> searchHospitals(@RequestBody HospitalRequest request) {
//        HospitalResponse response = new HospitalResponse();
//        try {
//            List<Hospital> hospitals = hospitalService.searchByRequest(request);
//            if (hospitals != null && !hospitals.isEmpty()) {
//                // Nếu tìm theo tên, có thể trả về nhiều chi nhánh (cùng tên, khác địa chỉ)
//                if (request.getHospitalName() != null && request.getHospitalId() == null) {
//                    response.setStatus(true);
//                    response.setMessage("Found " + hospitals.size() + " hospital branch(es) with name: " + request.getHospitalName());
//                } else {
//                    response.setStatus(true);
//                    response.setMessage("Hospitals found: " + hospitals.size());
//                }
//                response.setResult(hospitals);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No hospitals found");
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error searching hospitals: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to search hospitals: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
//
    @PostMapping("/")
    public ApiResponse<Hospital> createHospital(@RequestBody HospitalCreateRequest request) {
        Hospital hospital = hospitalService.save(request);
        return ApiResponse.<Hospital>builder()
                .result(hospital)
                .build();
    }

    @PutMapping("/{hospitalId}")
    public ApiResponse<Hospital> updateHospital(
            @PathVariable String hospitalId,
            @RequestBody HospitalCreateRequest request) {
        Hospital hospital = hospitalService.update(hospitalId, request);
        return ApiResponse.<Hospital>builder()
                .result(hospital)
                .build();
    }

    @DeleteMapping("/{hospitalId}")
    public ApiResponse deleteHospital(@PathVariable String hospitalId) {
        hospitalService.deleteById(hospitalId);
        return ApiResponse.builder()
                .message("Hospital " + hospitalId + " deleted successfully")
                .build();
    }
}

