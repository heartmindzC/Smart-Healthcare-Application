package com.example.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO để nhận thông tin bệnh viện từ Hospital Service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDetailResponse {
    private Boolean status;
    private String message;
    private List<HospitalData> result;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HospitalData {
        private Integer hospitalId;
        private String hospitalName;
        private String hospitalAddress;  // Field name từ Hospital Service
        private String hospitalPhone;
        private String hospitalEmail;
    }
    
    /**
     * Lấy địa chỉ bệnh viện, trả về null nếu không có
     */
    public String getAddress() {
        if (result != null && !result.isEmpty()) {
            HospitalData hospitalData = result.get(0);
            if (hospitalData != null && hospitalData.getHospitalAddress() != null 
                && !hospitalData.getHospitalAddress().trim().isEmpty()) {
                return hospitalData.getHospitalAddress();
            }
        }
        return null;
    }
}
