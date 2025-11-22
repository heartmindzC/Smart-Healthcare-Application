package com.example.doctorservice.repository;


import com.example.doctorservice.model.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor,Integer> {
    List<Doctor> findDoctorByDepartment(String department);
    List<Doctor> findDoctorByFullName(String fullName);
    List<Doctor> findDoctorByDoctorId(int doctorId);
    List<Doctor> findDoctorByUserId(int userId);
    List<Doctor> findDoctorByHospitalId(int hospitalId);
    List<Doctor> findDoctorByHospitalIdAndDepartment(int hospitalId, String department);
}
