package com.example.doctorservice.controller;

import com.example.doctorservice.dto.request.DoctorCreationRequest;
import com.example.doctorservice.dto.request.DoctorUpdateRequest;
import com.example.doctorservice.dto.response.ApiResponse;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/")
    public ApiResponse<Doctor> create(@Valid @RequestBody DoctorCreationRequest request) {
        Doctor doctor = doctorService.save(request);
        return ApiResponse.<Doctor>builder()
                .result(doctor)
                .build();
    }

    @PutMapping("/{doctorId}")
    public ApiResponse<Doctor> update(@PathVariable String doctorId, @Valid @RequestBody DoctorUpdateRequest request) {
        Doctor doctor = doctorService.update(doctorId, request);
        return ApiResponse.<Doctor>builder()
                .result(doctor)
                .build();
    }

    @GetMapping("/")
    public ApiResponse<List<Doctor>> findAll() {
        List<Doctor> doctors = doctorService.findAll();
        return ApiResponse.<List<Doctor>>builder()
                .result(doctors)
                .build();
    }

    @GetMapping("/{doctorId}")
    public ApiResponse<Doctor> findDoctorByDoctorId(@PathVariable String doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        return ApiResponse.<Doctor>builder()
                .result(doctor)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Doctor> findDoctorByUserId(@PathVariable String userId) {
        Doctor doctor = doctorService.findByUserId(userId);
        return  ApiResponse.<Doctor>builder()
                .result(doctor)
                .build();
    }

    @GetMapping("/hospital/{hospitalId}")
    public ApiResponse<List<Doctor>> findDoctorByHospitalId(@PathVariable String hospitalId) {
        List<Doctor> doctors = doctorService.findByHospitalId(hospitalId);
        return ApiResponse.<List<Doctor>>builder()
                .result(doctors)
                .build();
    }

    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<Doctor>> findDoctorByDepartment(@PathVariable String departmentId) {
        List<Doctor> doctors = doctorService.findByDepartment(departmentId);
        return ApiResponse.<List<Doctor>>builder()
                .result(doctors)
                .build();
    }

//    @GetMapping("/get-doctor-by-fullname/{fullName}")
//    public ResponseEntity<DoctorResponse> findDoctorByFullName(@PathVariable String fullName) {
//        DoctorResponse response = new DoctorResponse();
//        try {
//            List<Doctor> doctors = doctorService.findByFullName(fullName);
//            if (doctors != null && !doctors.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Doctors found: " + doctors.size());
//                response.setResult(doctors);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No doctors found with name: " + fullName);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error finding doctors by fullName: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to retrieve doctors: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }

//    @PostMapping("/search-doctors")
//    public ResponseEntity<DoctorResponse> searchDoctors(@RequestBody DoctorRequest request) {
//        DoctorResponse response = new DoctorResponse();
//        try {
//            List<Doctor> doctors = doctorService.searchByHospitalIdAndDepartment(request);
//            if (doctors != null && !doctors.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Doctors found: " + doctors.size());
//                response.setResult(doctors);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No doctors found with hospitalId: " + request.getHospitalId() +
//                    " and department: " + request.getDepartment());
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error searching doctors: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to search doctors: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
}
