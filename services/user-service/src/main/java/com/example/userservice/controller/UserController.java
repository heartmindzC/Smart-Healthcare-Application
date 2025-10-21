package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findUserByUserId/{userId}")
    public ResponseEntity<UserDTO> findUserByUserId(@PathVariable("userId") String userId)
    throws Exception {
        UserDTO user = userService.findByUserId(userId);
        if (user != null) {
            return ResponseEntity.ok(user); // Trả về UserDTO nếu tìm thấy
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy
        }
    }
}
