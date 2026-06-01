package com.example.appointmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.example.appointmentservice",      // Package của service hiện tại
        "com.example.common_exception"  // Package của thư viện common
})
@EnableScheduling  // Enable scheduled tasks for PENDING timeout processing
@EnableAsync       // Enable async processing for notification listeners
public class AppointmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }

}
