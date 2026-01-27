package com.example.doctorservice.repository;


import com.example.doctorservice.model.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor,String> {
    List<Doctor> findDoctorByDepartment(String department);
    List<Doctor> findDoctorByFullName(String fullName);
    List<Doctor> findDoctorByDoctorId(String doctorId);
    Optional<Doctor> findDoctorByUserId(String userId);
    List<Doctor> findDoctorByHospitalId(String hospitalId);
    List<Doctor> findDoctorByHospitalIdAndDepartment(String hospitalId, String department);

    boolean existsByUserId(String userId);
}
