package com.example.appointmentservice.model;

public enum AppointmentStatus {
    PENDING,      // Chờ xác nhận
    CONFIRMED,    // Đã xác nhận
    CANCELLED,    // Đã hủy
    COMPLETED,    // Đã hoàn thành
    NO_SHOW       // Bệnh nhân không đến
}

