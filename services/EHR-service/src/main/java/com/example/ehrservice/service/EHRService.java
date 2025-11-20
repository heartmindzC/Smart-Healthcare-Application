package com.example.ehrservice.service;

import com.example.ehrservice.dto.DoctorDTO;
import com.example.ehrservice.dto.EHRResponse;
import com.example.ehrservice.dto.PatientDTO;
import com.example.ehrservice.model.EHR;
import com.example.ehrservice.model.MedicalVisit;
import com.example.ehrservice.model.Prescription;
import com.example.ehrservice.repository.MedicalVisitRepository;
import com.example.ehrservice.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EHRService {
    
    @Autowired
    private MedicalVisitRepository visitRepository;
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${services.patient.url:http://localhost:8081}")
    private String patientServiceUrl;
    
    @Value("${services.doctor.url:http://localhost:8082}")
    private String doctorServiceUrl;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public EHRResponse getEHRByPatientId(Integer patientId) {
        EHRResponse response = new EHRResponse();
        EHR ehr = new EHR();
        StringBuilder messageBuilder = new StringBuilder();
        boolean hasError = false;
        
        // 1. Lấy thông tin bệnh nhân từ patient-service (có thể lỗi nhưng vẫn tiếp tục)
        try {
            String patientUrl = patientServiceUrl + "/patients/get-patient/" + patientId;
            PatientDTO patient = restTemplate.getForObject(patientUrl, PatientDTO.class);
            
            if (patient != null) {
                EHR.PatientInfo patientInfo = new EHR.PatientInfo();
                patientInfo.setPatientId(patient.getPatientId());
                patientInfo.setFullName(patient.getFullName());
                patientInfo.setUserId(patient.getUserId());
                if (patient.getBirth() != null) {
                    patientInfo.setBirth(dateFormat.format(patient.getBirth()));
                }
                patientInfo.setGender(patient.getGender());
                patientInfo.setBloodType(patient.getBloodType());
                patientInfo.setHeights(patient.getHeights());
                patientInfo.setWeights(patient.getWeights());
                patientInfo.setInsuranceId(patient.getInsuranceId());
                patientInfo.setEmergencyCallingNumber(patient.getEmergencyCallingNumber());
                patientInfo.setJob(patient.getJob());
                ehr.setPatientInfo(patientInfo);
            }
        } catch (Exception e) {
            System.err.println("Error fetching patient info: " + e.getMessage());
            e.printStackTrace();
            hasError = true;
            messageBuilder.append("Failed to fetch patient info: ").append(e.getMessage()).append("; ");
            // Không set patientInfo, để null
        }
        
        // 2. Lấy lịch sử khám bệnh (luôn cố gắng lấy, không bị ảnh hưởng bởi lỗi patient-service)
        try {
            List<MedicalVisit> visits = visitRepository.findByPatientIdOrderByVisitDateDesc(patientId);
            List<EHR.MedicalVisitInfo> visitInfos = visits.stream().map(visit -> {
                EHR.MedicalVisitInfo visitInfo = new EHR.MedicalVisitInfo();
                visitInfo.setVisitId(visit.getVisitId());
                // visitInfo.setDoctorId(visit.getDoctorId());
                
                // // Lấy thông tin bác sĩ từ doctor-service (có thể lỗi nhưng vẫn tiếp tục)
                // try {
                //     String doctorUrl = doctorServiceUrl + "/doctors/" + visit.getDoctorId();
                //     DoctorDTO doctor = restTemplate.getForObject(doctorUrl, DoctorDTO.class);
                //     if (doctor != null) {
                //         visitInfo.setDoctorName(doctor.getFullName());
                //     } else {
                //         visitInfo.setDoctorName("Unknown");
                //     }
                // } catch (Exception e) {
                //     System.err.println("Error fetching doctor info for doctorId " + visit.getDoctorId() + ": " + e.getMessage());
                //     visitInfo.setDoctorName("Unknown");
                // }
                
                visitInfo.setVisitDate(visit.getVisitDate());
                visitInfo.setHospital(visit.getHospital());
                visitInfo.setDepartment(visit.getDepartment());
                visitInfo.setDiagnosis(visit.getDiagnosis());
                
                // Lấy đơn thuốc cho lần khám này
                List<Prescription> prescriptions = prescriptionRepository.findByVisitId(visit.getVisitId());
                List<EHR.PrescriptionInfo> prescriptionInfos = prescriptions.stream()
                    .map(this::mapToPrescriptionInfo)
                    .collect(Collectors.toList());
                visitInfo.setPrescriptions(prescriptionInfos);
                
                return visitInfo;
            }).collect(Collectors.toList());
            ehr.setMedicalHistory(visitInfos);
        } catch (Exception e) {
            System.err.println("Error fetching medical visits: " + e.getMessage());
            e.printStackTrace();
            hasError = true;
            messageBuilder.append("Failed to fetch medical visits: ").append(e.getMessage()).append("; ");
            ehr.setMedicalHistory(new ArrayList<>());
        }
        
        // 3. Lấy tất cả đơn thuốc (luôn cố gắng lấy)
        try {
            List<Prescription> allPrescriptions = prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId);
            List<EHR.PrescriptionInfo> allPrescriptionInfos = allPrescriptions.stream()
                .map(this::mapToPrescriptionInfo)
                .collect(Collectors.toList());
            ehr.setAllPrescriptions(allPrescriptionInfos);
        } catch (Exception e) {
            System.err.println("Error fetching prescriptions: " + e.getMessage());
            e.printStackTrace();
            hasError = true;
            messageBuilder.append("Failed to fetch prescriptions: ").append(e.getMessage()).append("; ");
            ehr.setAllPrescriptions(new ArrayList<>());
        }
        
        // Set response - luôn trả về kết quả nếu có ít nhất một phần thành công
        if (ehr.getMedicalHistory() != null && !ehr.getMedicalHistory().isEmpty() 
            || ehr.getAllPrescriptions() != null && !ehr.getAllPrescriptions().isEmpty()
            || ehr.getPatientInfo() != null) {
            response.setStatus(true);
            if (hasError) {
                response.setMessage("EHR retrieved with some errors: " + messageBuilder.toString().trim());
            } else {
                response.setMessage("EHR retrieved successfully");
            }
        } else {
            // Tất cả đều lỗi
            response.setStatus(false);
            response.setMessage("Failed to retrieve EHR: " + messageBuilder.toString().trim());
        }
        response.setResult(ehr);
        
        return response;
    }
    
    public EHRResponse getEHRByUserId(String userId) {
        EHRResponse response = new EHRResponse();
        
        try {
            // Lấy patientId từ userId
            String patientUrl = patientServiceUrl + "/patients/get-patient-by-user-id/" + userId;
            PatientDTO patient = restTemplate.getForObject(patientUrl, PatientDTO.class);
            
            if (patient != null && patient.getPatientId() != null) {
                return getEHRByPatientId(patient.getPatientId());
            } else {
                // Không tìm thấy patient với userId này, nhưng vẫn thử lấy visits và prescriptions nếu có patientId từ database
                // Tìm patientId từ medical_visits hoặc prescriptions
                List<MedicalVisit> visits = visitRepository.findAll();
                Integer foundPatientId = visits.stream()
                    .filter(v -> v.getPatientId() != null)
                    .map(MedicalVisit::getPatientId)
                    .findFirst()
                    .orElse(null);
                
                if (foundPatientId != null) {
                    // Thử với patientId tìm được
                    return getEHRByPatientId(foundPatientId);
                } else {
                    // Không tìm thấy patient với userId này và không có data nào
                    response.setStatus(false);
                    response.setMessage("Patient not found with userId: " + userId);
                    EHR emptyEhr = new EHR();
                    emptyEhr.setMedicalHistory(new ArrayList<>());
                    emptyEhr.setAllPrescriptions(new ArrayList<>());
                    response.setResult(emptyEhr);
                    return response;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching patient by userId: " + e.getMessage());
            e.printStackTrace();
            
            // Vẫn cố gắng lấy visits và prescriptions nếu có thể
            // Tìm patientId từ database
            try {
                List<MedicalVisit> visits = visitRepository.findAll();
                Integer foundPatientId = visits.stream()
                    .filter(v -> v.getPatientId() != null)
                    .map(MedicalVisit::getPatientId)
                    .findFirst()
                    .orElse(null);
                
                if (foundPatientId != null) {
                    return getEHRByPatientId(foundPatientId);
                }
            } catch (Exception ex) {
                System.err.println("Error finding patientId from database: " + ex.getMessage());
            }
            
            // Nếu không tìm được, trả về response rỗng
            response.setStatus(false);
            response.setMessage("Failed to retrieve EHR: " + e.getMessage());
            EHR emptyEhr = new EHR();
            emptyEhr.setMedicalHistory(new ArrayList<>());
            emptyEhr.setAllPrescriptions(new ArrayList<>());
            response.setResult(emptyEhr);
            return response;
        }
    }
    
    private EHR.PrescriptionInfo mapToPrescriptionInfo(Prescription p) {
        EHR.PrescriptionInfo pi = new EHR.PrescriptionInfo();
        pi.setPrescriptionId(p.getPrescriptionId());
        pi.setMedicationName(p.getMedicationName());
        pi.setDosage(p.getDosage());
        pi.setFrequency(p.getFrequency());
        pi.setQuantity(p.getQuantity());
        pi.setInstructions(p.getInstructions());
        pi.setPrescribedDate(p.getPrescribedDate());
        return pi;
    }
}

