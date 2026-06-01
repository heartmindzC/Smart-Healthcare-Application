package com.example.hospitalservice.repository;

import com.example.hospitalservice.model.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, String> {
    List<Department> findDepartmentByDepartmentName(String departmentName);
    List<Department> findDepartmentByHospitalId(String hospitalId);
    Optional<Department> findByDepartmentId(String departmentId);
    List<Department> findDepartmentByDepartmentNameContaining(String departmentName);
}


