package com.example.appointmentservice.client;

import com.example.appointmentservice.dto.HospitalDetailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Client để gọi Hospital Service thông qua API Gateway/trực tiếp
 */
@Component
public class HospitalServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(HospitalServiceClient.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${services.hospital.url:http://localhost:8084}")
    private String hospitalServiceUrl;
    
    /**
     * Lấy địa chỉ bệnh viện từ Hospital Service
     * @param hospitalId ID của bệnh viện
     * @return Optional chứa địa chỉ bệnh viện, empty nếu không lấy được
     */
    public Optional<String> getHospitalAddress(int hospitalId) {
        try {
            String url = hospitalServiceUrl + "/hospitals/get-hospital/" + hospitalId;
            logger.info("Fetching hospital address from: {}", url);
            
            HospitalDetailResponse response = restTemplate.getForObject(url, HospitalDetailResponse.class);
            
            if (response != null && response.getStatus() != null && response.getStatus()) {
                String address = response.getAddress();
                if (address != null) {
                    logger.info("Successfully fetched hospital address for hospitalId: {}", hospitalId);
                    return Optional.of(address);
                } else {
                    logger.warn("Hospital address is null for hospitalId: {}", hospitalId);
                }
            } else {
                logger.warn("Failed to fetch hospital details. Response status: {}", 
                    response != null ? response.getStatus() : "null");
            }
            
        } catch (Exception e) {
            logger.error("Error fetching hospital address for hospitalId: {}. Error: {}", 
                hospitalId, e.getMessage());
        }
        
        return Optional.empty();
    }
}
