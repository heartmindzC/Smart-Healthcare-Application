package com.example.hospitalservice.controller;

import com.example.hospitalservice.dto.HospitalCreateRequest;
import com.example.hospitalservice.dto.HospitalRequest;
import com.example.hospitalservice.dto.HospitalResponse;
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

    @GetMapping("/all")
    public ResponseEntity<HospitalResponse> findAll() {
        HospitalResponse response = new HospitalResponse();
        try {
            List<Hospital> hospitals = hospitalService.findAll();
            if (hospitals.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No hospitals found");
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Hospitals found: " + hospitals.size());
                response.setResult(hospitals);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error finding all hospitals: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve hospitals: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-hospital/{hospitalId}")
    public ResponseEntity<HospitalResponse> findHospitalById(@PathVariable int hospitalId) {
        HospitalResponse response = new HospitalResponse();
        try {
            Optional<Hospital> hospitalOptional = hospitalService.findById(hospitalId);
            if (hospitalOptional.isPresent()) {
                response.setStatus(true);
                response.setMessage("Hospital found");
                response.setResult(List.of(hospitalOptional.get()));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Hospital not found with ID: " + hospitalId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding hospital by ID: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve hospital: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-hospital-by-name/{hospitalName}")
    public ResponseEntity<HospitalResponse> findHospitalByName(@PathVariable String hospitalName) {
        HospitalResponse response = new HospitalResponse();
        try {
            // Tìm theo tên bệnh viện - trả về list các chi nhánh (cùng tên, khác địa chỉ)
            List<Hospital> hospitals = hospitalService.findByHospitalName(hospitalName);
            if (hospitals != null && !hospitals.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Found " + hospitals.size() + " hospital branch(es) with name: " + hospitalName);
                response.setResult(hospitals);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No hospitals found with name: " + hospitalName);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding hospitals by name: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve hospitals: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/search-by-address/{address}")
    public ResponseEntity<HospitalResponse> searchHospitalsByAddress(@PathVariable String address) {
        HospitalResponse response = new HospitalResponse();
        try {
            List<Hospital> hospitals = hospitalService.findByAddressContaining(address);
            if (hospitals != null && !hospitals.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Hospitals found: " + hospitals.size());
                response.setResult(hospitals);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No hospitals found with address containing: " + address);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error searching hospitals by address: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to search hospitals: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<HospitalResponse> searchHospitals(@RequestBody HospitalRequest request) {
        HospitalResponse response = new HospitalResponse();
        try {
            List<Hospital> hospitals = hospitalService.searchByRequest(request);
            if (hospitals != null && !hospitals.isEmpty()) {
                // Nếu tìm theo tên, có thể trả về nhiều chi nhánh (cùng tên, khác địa chỉ)
                if (request.getHospitalName() != null && request.getHospitalId() == null) {
                    response.setStatus(true);
                    response.setMessage("Found " + hospitals.size() + " hospital branch(es) with name: " + request.getHospitalName());
                } else {
                    response.setStatus(true);
                    response.setMessage("Hospitals found: " + hospitals.size());
                }
                response.setResult(hospitals);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No hospitals found");
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error searching hospitals: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to search hospitals: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody HospitalCreateRequest request) {
        HospitalResponse response = new HospitalResponse();
        try {
            Hospital hospital = new Hospital();
            hospital.setHospitalName(request.getHospitalName());
            hospital.setHospitalAddress(request.getHospitalAddress());
            hospital.setHospitalPhone(request.getHospitalPhone());
            hospital.setHospitalEmail(request.getHospitalEmail());
            
            Hospital savedHospital = hospitalService.save(hospital);
            response.setStatus(true);
            response.setMessage("Hospital created successfully");
            response.setResult(List.of(savedHospital));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("Error creating hospital: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to create hospital: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/update/{hospitalId}")
    public ResponseEntity<HospitalResponse> updateHospital(
            @PathVariable int hospitalId,
            @RequestBody HospitalCreateRequest request) {
        HospitalResponse response = new HospitalResponse();
        try {
            Optional<Hospital> hospitalOptional = hospitalService.findById(hospitalId);
            if (hospitalOptional.isPresent()) {
                Hospital hospital = hospitalOptional.get();
                hospital.setHospitalName(request.getHospitalName());
                hospital.setHospitalAddress(request.getHospitalAddress());
                hospital.setHospitalPhone(request.getHospitalPhone());
                hospital.setHospitalEmail(request.getHospitalEmail());
                
                Hospital updatedHospital = hospitalService.save(hospital);
                response.setStatus(true);
                response.setMessage("Hospital updated successfully");
                response.setResult(List.of(updatedHospital));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Hospital not found with ID: " + hospitalId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error updating hospital: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to update hospital: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/delete/{hospitalId}")
    public ResponseEntity<HospitalResponse> deleteHospital(@PathVariable int hospitalId) {
        HospitalResponse response = new HospitalResponse();
        try {
            Optional<Hospital> hospitalOptional = hospitalService.findById(hospitalId);
            if (hospitalOptional.isPresent()) {
                hospitalService.deleteById(hospitalId);
                response.setStatus(true);
                response.setMessage("Hospital deleted successfully");
                response.setResult(null);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Hospital not found with ID: " + hospitalId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error deleting hospital: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to delete hospital: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}

