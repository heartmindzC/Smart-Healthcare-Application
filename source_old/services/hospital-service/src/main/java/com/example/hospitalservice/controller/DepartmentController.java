package com.example.hospitalservice.controller;

import com.example.hospitalservice.dto.DepartmentCreateRequest;
import com.example.hospitalservice.dto.DepartmentRequest;
import com.example.hospitalservice.dto.DepartmentResponse;
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

    @GetMapping("/all")
    public ResponseEntity<DepartmentResponse> findAll() {
        DepartmentResponse response = new DepartmentResponse();
        try {
            List<Department> departments = departmentService.findAll();
            if (departments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No departments found");
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Departments found: " + departments.size());
                response.setResult(departments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error finding all departments: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve departments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-department/{departmentId}")
    public ResponseEntity<DepartmentResponse> findDepartmentById(@PathVariable int departmentId) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            Optional<Department> departmentOptional = departmentService.findById(departmentId);
            if (departmentOptional.isPresent()) {
                response.setStatus(true);
                response.setMessage("Department found");
                response.setResult(List.of(departmentOptional.get()));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Department not found with ID: " + departmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding department by ID: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve department: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-department-by-name/{departmentName}")
    public ResponseEntity<DepartmentResponse> findDepartmentByName(@PathVariable String departmentName) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            List<Department> departments = departmentService.findByDepartmentName(departmentName);
            if (departments != null && !departments.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Departments found: " + departments.size());
                response.setResult(departments);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No departments found with name: " + departmentName);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding departments by name: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve departments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/get-departments-by-hospital/{hospitalId}")
    public ResponseEntity<DepartmentResponse> findDepartmentsByHospitalId(@PathVariable int hospitalId) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            List<Department> departments = departmentService.findByHospitalId(hospitalId);
            if (departments != null && !departments.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Departments found: " + departments.size());
                response.setResult(departments);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No departments found for hospital ID: " + hospitalId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error finding departments by hospital ID: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to retrieve departments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/search-by-name/{departmentName}")
    public ResponseEntity<DepartmentResponse> searchDepartmentsByName(@PathVariable String departmentName) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            List<Department> departments = departmentService.findByDepartmentNameContaining(departmentName);
            if (departments != null && !departments.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Departments found: " + departments.size());
                response.setResult(departments);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No departments found with name containing: " + departmentName);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error searching departments by name: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to search departments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<DepartmentResponse> searchDepartments(@RequestBody DepartmentRequest request) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            List<Department> departments = departmentService.searchByRequest(request);
            if (departments != null && !departments.isEmpty()) {
                response.setStatus(true);
                response.setMessage("Departments found: " + departments.size());
                response.setResult(departments);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("No departments found");
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error searching departments: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to search departments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            Department department = new Department();
            department.setDepartmentName(request.getDepartmentName());
            department.setDepartmentPhone(request.getDepartmentPhone());
            department.setDepartmentEmail(request.getDepartmentEmail());
            department.setHospitalId(request.getHospitalId());
            
            Department savedDepartment = departmentService.save(department);
            response.setStatus(true);
            response.setMessage("Department created successfully");
            response.setResult(List.of(savedDepartment));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("Error creating department: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to create department: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/update/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable int departmentId,
            @RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            Optional<Department> departmentOptional = departmentService.findById(departmentId);
            if (departmentOptional.isPresent()) {
                Department department = departmentOptional.get();
                department.setDepartmentName(request.getDepartmentName());
                department.setDepartmentPhone(request.getDepartmentPhone());
                department.setDepartmentEmail(request.getDepartmentEmail());
                department.setHospitalId(request.getHospitalId());
                
                Department updatedDepartment = departmentService.save(department);
                response.setStatus(true);
                response.setMessage("Department updated successfully");
                response.setResult(List.of(updatedDepartment));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Department not found with ID: " + departmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error updating department: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to update department: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<DepartmentResponse> deleteDepartment(@PathVariable int departmentId) {
        DepartmentResponse response = new DepartmentResponse();
        try {
            Optional<Department> departmentOptional = departmentService.findById(departmentId);
            if (departmentOptional.isPresent()) {
                departmentService.deleteById(departmentId);
                response.setStatus(true);
                response.setMessage("Department deleted successfully");
                response.setResult(null);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Department not found with ID: " + departmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("Error deleting department: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Failed to delete department: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}

