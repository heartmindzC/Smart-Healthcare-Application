package com.example.hospitalservice.service;

import com.example.hospitalservice.dto.request.DepartmentCreateRequest;
import com.example.hospitalservice.dto.request.DepartmentRequest;
import com.example.hospitalservice.mapper.DepartmentMapper;
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
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> findAll() {
        return (List<Department>) departmentRepository.findAll();
    }

    public Department findById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

//    public List<Department> findByDepartmentName(String departmentName) {
//        return departmentRepository.findDepartmentByDepartmentName(departmentName);
//    }

    public List<Department> findByHospitalId(String hospitalId) {
        return departmentRepository.findDepartmentByHospitalId(hospitalId);
    }
//
//    public List<Department> findByDepartmentNameContaining(String departmentName) {
//        return departmentRepository.findDepartmentByDepartmentNameContaining(departmentName);
//    }
//
//    public List<Department> searchByRequest(DepartmentRequest request) {
//        List<Department> results = new ArrayList<>();
//
//        if (request.getHospitalId() > 0 && request.getDepartmentName() != null && !request.getDepartmentName().isEmpty()) {
//            // Search by both hospitalId and departmentName
//            List<Department> byHospital = departmentRepository.findDepartmentByHospitalId(request.getHospitalId());
//            for (Department dept : byHospital) {
//                if (dept.getDepartmentName().equalsIgnoreCase(request.getDepartmentName()) ||
//                    dept.getDepartmentName().toLowerCase().contains(request.getDepartmentName().toLowerCase())) {
//                    results.add(dept);
//                }
//            }
//        } else if (request.getHospitalId() > 0) {
//            // Search by hospitalId only
//            results = departmentRepository.findDepartmentByHospitalId(request.getHospitalId());
//        } else if (request.getDepartmentName() != null && !request.getDepartmentName().isEmpty()) {
//            // Search by departmentName only
//            results = departmentRepository.findDepartmentByDepartmentNameContaining(request.getDepartmentName());
//        }
//
//        return results;
//    }

    public Department save(DepartmentCreateRequest request) {
        Department department = departmentMapper.toDepartment(request);
        return departmentRepository.save(department);
    }

    public void deleteById(String id) {
        departmentRepository.deleteById(id);
    }

    public Department update(String id, DepartmentCreateRequest request) {
        Department department = findById(id);

        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentPhone(request.getDepartmentPhone());
        department.setDepartmentEmail(request.getDepartmentEmail());
        department.setHospitalId(request.getHospitalId());
        return departmentRepository.save(department);
    }
}

