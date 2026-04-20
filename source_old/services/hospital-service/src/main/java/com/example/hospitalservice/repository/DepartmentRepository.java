package com.example.hospitalservice.repository;

import com.example.hospitalservice.model.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    List<Department> findDepartmentByDepartmentName(String departmentName);
    List<Department> findDepartmentByHospitalId(int hospitalId);
    Optional<Department> findByDepartmentId(int departmentId);
    List<Department> findDepartmentByDepartmentNameContaining(String departmentName);
}


