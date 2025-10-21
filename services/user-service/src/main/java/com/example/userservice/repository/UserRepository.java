package com.example.userservice.repository;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDTO,String> {
    Optional<UserDTO> findByUserId(String userId);
   Optional<UserDTO> findByEmail(String email);
   Optional<UserDTO> findByPhone(String phone);


}
