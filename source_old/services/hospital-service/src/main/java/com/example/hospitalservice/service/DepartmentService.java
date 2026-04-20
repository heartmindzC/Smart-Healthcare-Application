package com.example.hospitalservice.service;

import com.example.hospitalservice.dto.DepartmentRequest;
import com.example.hospitalservice.model.Department;
import com.example.hospitalservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return (List<Department>) departmentRepository.findAll();
    }

    public Optional<Department> findById(int id) {
        return departmentRepository.findById(id);
    }

    public List<Department> findByDepartmentName(String departmentName) {
        return departmentRepository.findDepartmentByDepartmentName(departmentName);
    }

    public List<Department> findByHospitalId(int hospitalId) {
        return departmentRepository.findDepartmentByHospitalId(hospitalId);
    }

    public List<Department> findByDepartmentNameContaining(String departmentName) {
        return departmentRepository.findDepartmentByDepartmentNameContaining(departmentName);
    }

    public List<Department> searchByRequest(DepartmentRequest request) {
        List<Department> results = new ArrayList<>();
        
        if (request.getHospitalId() > 0 && request.getDepartmentName() != null && !request.getDepartmentName().isEmpty()) {
            // Search by both hospitalId and departmentName
            List<Department> byHospital = departmentRepository.findDepartmentByHospitalId(request.getHospitalId());
            for (Department dept : byHospital) {
                if (dept.getDepartmentName().equalsIgnoreCase(request.getDepartmentName()) ||
                    dept.getDepartmentName().toLowerCase().contains(request.getDepartmentName().toLowerCase())) {
                    results.add(dept);
                }
            }
        } else if (request.getHospitalId() > 0) {
            // Search by hospitalId only
            results = departmentRepository.findDepartmentByHospitalId(request.getHospitalId());
        } else if (request.getDepartmentName() != null && !request.getDepartmentName().isEmpty()) {
            // Search by departmentName only
            results = departmentRepository.findDepartmentByDepartmentNameContaining(request.getDepartmentName());
        }
        
        return results;
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteById(int id) {
        departmentRepository.deleteById(id);
    }
}

