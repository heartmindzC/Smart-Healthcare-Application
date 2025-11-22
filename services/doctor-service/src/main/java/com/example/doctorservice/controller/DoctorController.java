package com.example.doctorservice.controller;

import com.example.doctorservice.dto.DoctorRequest;
import com.example.doctorservice.dto.DoctorResponse;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/all")
    public ResponseEntity<DoctorResponse> findAll() {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.findAll();
            if (doctors.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No doctors found");
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Doctors found: " + doctors.size());
                response.setResult(doctors);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error finding all doctors: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctors: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-doctor/{doctorId}")
    public ResponseEntity<DoctorResponse> findDoctorByDoctorId(@PathVariable int doctorId) {
        DoctorResponse response = new DoctorResponse();
        try {
            Optional<Doctor> doctorOptional = doctorService.findById(doctorId);
            if (doctorOptional.isPresent()) {
                response.setStatus(true);
                response.setMessage("Doctor found");
                response.setResult(List.of(doctorOptional.get()));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Doctor not found with ID: " + doctorId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding doctor by ID: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctor: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-doctor-by-user-id/{userId}")
    public ResponseEntity<DoctorResponse> findDoctorByUserId(@PathVariable int userId) {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.findByUserId(userId);
            if (doctors != null && !doctors.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Doctor found");
                response.setResult(doctors);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Doctor not found with userId: " + userId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding doctor by userId: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctor: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-doctor-by-hospital-id/{hospitalId}")
    public ResponseEntity<DoctorResponse> findDoctorByHospitalId(@PathVariable int hospitalId) {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.findByHospitalId(hospitalId);
            if (doctors != null && !doctors.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Doctors found: " + doctors.size());
                response.setResult(doctors);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No doctors found with hospitalId: " + hospitalId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding doctors by hospitalId: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctors: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-doctor-by-department/{department}")
    public ResponseEntity<DoctorResponse> findDoctorByDepartment(@PathVariable String department) {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.findByDepartment(department);
            if (doctors != null && !doctors.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Doctors found: " + doctors.size());
                response.setResult(doctors);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No doctors found in department: " + department);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding doctors by department: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctors: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-doctor-by-fullname/{fullName}")
    public ResponseEntity<DoctorResponse> findDoctorByFullName(@PathVariable String fullName) {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.findByFullName(fullName);
            if (doctors != null && !doctors.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Doctors found: " + doctors.size());
                response.setResult(doctors);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No doctors found with name: " + fullName);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding doctors by fullName: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve doctors: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/search-doctors")
    public ResponseEntity<DoctorResponse> searchDoctors(@RequestBody DoctorRequest request) {
        DoctorResponse response = new DoctorResponse();
        try {
            List<Doctor> doctors = doctorService.searchByHospitalIdAndDepartment(request);
            if (doctors != null && !doctors.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Doctors found: " + doctors.size());
                response.setResult(doctors);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No doctors found with hospitalId: " + request.getHospitalId() + 
                    " and department: " + request.getDepartment());
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error searching doctors: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to search doctors: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}
