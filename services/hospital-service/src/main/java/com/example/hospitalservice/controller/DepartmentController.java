package com.example.hospitalservice.controller;

import com.example.hospitalservice.dto.request.DepartmentCreateRequest;
import com.example.hospitalservice.dto.request.DepartmentRequest;
import com.example.hospitalservice.dto.response.ApiResponse;
import com.example.hospitalservice.dto.response.DepartmentResponse;
import com.example.hospitalservice.model.Department;
import com.example.hospitalservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public ApiResponse<List<Department>> findAll() {
        List<Department> departments = departmentService.findAll();
        return ApiResponse.<List<Department>>builder()
                .result(departments)
                .build();
    }

    @GetMapping("/{departmentId}")
    public ApiResponse<Department> findDepartmentById(@PathVariable String departmentId) {
        Department department = departmentService.findById(departmentId);
        return ApiResponse.<Department>builder()
                .result(department)
                .build();
    }

//    @GetMapping("/get-department-by-name/{departmentName}")
//    public ResponseEntity<DepartmentResponse> findDepartmentByName(@PathVariable String departmentName) {
//        DepartmentResponse response = new DepartmentResponse();
//        try {
//            List<Department> departments = departmentService.findByDepartmentName(departmentName);
//            if (departments != null && !departments.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Departments found: " + departments.size());
//                response.setResult(departments);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No departments found with name: " + departmentName);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error finding departments by name: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to retrieve departments: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
//
    @GetMapping("/hospital/{hospitalId}")
    public ApiResponse<List<Department>> findDepartmentsByHospitalId(@PathVariable String hospitalId) {
        List<Department> response = departmentService.findByHospitalId(hospitalId);
        return  ApiResponse.<List<Department>>builder()
                .result(response)
                .build();
    }
//
//    @GetMapping("/search-by-name/{departmentName}")
//    public ResponseEntity<DepartmentResponse> searchDepartmentsByName(@PathVariable String departmentName) {
//        DepartmentResponse response = new DepartmentResponse();
//        try {
//            List<Department> departments = departmentService.findByDepartmentNameContaining(departmentName);
//            if (departments != null && !departments.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Departments found: " + departments.size());
//                response.setResult(departments);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No departments found with name containing: " + departmentName);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error searching departments by name: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to search departments: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity<DepartmentResponse> searchDepartments(@RequestBody DepartmentRequest request) {
//        DepartmentResponse response = new DepartmentResponse();
//        try {
//            List<Department> departments = departmentService.searchByRequest(request);
//            if (departments != null && !departments.isEmpty()) {
//                response.setStatus(true);
//                response.setMessage("Departments found: " + departments.size());
//                response.setResult(departments);
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus(false);
//                response.setMessage("No departments found");
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//        } catch (Exception e) {
//            System.err.println("Error searching departments: " + e.getMessage());
//            e.printStackTrace();
//            response.setStatus(false);
//            response.setMessage("Failed to search departments: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }

    @PostMapping("/")
    public ApiResponse<Department> createDepartment(@RequestBody DepartmentCreateRequest request) {
        Department department = departmentService.save(request);
        return ApiResponse.<Department>builder()
                .result(department)
                .build();
    }

    @PutMapping("/{departmentId}")
    public ApiResponse<Department> updateDepartment(
            @PathVariable String departmentId,
            @RequestBody DepartmentCreateRequest request) {
        Department department = departmentService.update(departmentId, request);
        return ApiResponse.<Department>builder()
                .result(department)
                .build();
    }

    @DeleteMapping("/{departmentId}")
    public ApiResponse deleteDepartment(@PathVariable String departmentId) {
        departmentService.deleteById(departmentId);
        return ApiResponse.<Department>builder()
                .message("Department " + departmentId + " deleted successfully")
                .build();
    }
}

