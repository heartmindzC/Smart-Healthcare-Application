package com.example.smart_healthcare_application;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.MedicalHistoryAdapter; // Nhớ kiểm tra đúng đường dẫn package của bạn
import com.example.smart_healthcare_application.models.EHR;
import com.example.smart_healthcare_application.models.MedicalVisits;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.Prescription;

import java.util.ArrayList;
import java.util.List;

public class EHRActivity extends AppCompatActivity {

    // Views Thông tin bệnh nhân
    private TextView tvEhrFullName, tvEhrPatientId, tvEhrBlood, tvEhrHeight, tvEhrWeight;
    private TextView tvEhrInsurance, tvEhrEmergency;
    private ImageButton btnBack;

    // --- PHẦN THÊM VÀO: Khai báo RecyclerView và Adapter ---
    private RecyclerView rvMedicalHistory;
    private MedicalHistoryAdapter medicalAdapter;
    private List<MedicalVisits> medicalVisitsList = new ArrayList<>();
    // -------------------------------------------------------

    // Đã xóa biến llMedicalHistoryContainer cũ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ehr);

        initViews();
        setupListeners();

        // Tạm thời dùng Mock Data, sau này thay bằng dữ liệu gọi từ API
        fillEhrData(getMockEhrData());
    }

    private void initViews() {
        tvEhrFullName = findViewById(R.id.tvEhrFullName);
        tvEhrPatientId = findViewById(R.id.tvEhrPatientId);
        tvEhrBlood = findViewById(R.id.tvEhrBlood);
        tvEhrHeight = findViewById(R.id.tvEhrHeight);
        tvEhrWeight = findViewById(R.id.tvEhrWeight);
        tvEhrInsurance = findViewById(R.id.tvEhrInsurance);
        tvEhrEmergency = findViewById(R.id.tvEhrEmergency);
        btnBack = findViewById(R.id.btnBack);

        // --- PHẦN THÊM VÀO: Ánh xạ và cấu hình RecyclerView ---
        rvMedicalHistory = findViewById(R.id.rvMedicalHistory);

        // Disable nested scrolling để cuộn mượt hơn khi nằm trong ScrollView tổng
        rvMedicalHistory.setNestedScrollingEnabled(false);

        // Setup LayoutManager
        rvMedicalHistory.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo và gán Adapter
        medicalAdapter = new MedicalHistoryAdapter(this, medicalVisitsList);
        rvMedicalHistory.setAdapter(medicalAdapter);
        // ------------------------------------------------------
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    public void fillEhrData(EHR ehrData) {
        if (ehrData == null) return;

        // 1. Fill Thông tin bệnh nhân
        Patient patient = ehrData.getPatientInfo();
        if (patient != null) {
            tvEhrFullName.setText(patient.getFullName() != null ? patient.getFullName() : "Chưa cập nhật");
            tvEhrPatientId.setText(patient.getPatientId() != null ? "Mã BN: " + patient.getPatientId() : "");
            tvEhrBlood.setText("Nhóm máu: " + (patient.getBloodType() != null ? patient.getBloodType() : "--"));
            tvEhrHeight.setText("Cao: " + (patient.getHeights() != null && patient.getHeights() > 0 ? patient.getHeights() + "cm" : "--"));
            tvEhrWeight.setText("Nặng: " + (patient.getWeights() != null && patient.getWeights() > 0 ? patient.getWeights() + "kg" : "--"));
            tvEhrInsurance.setText("BHYT: " + (patient.getInsuranceId() != null ? patient.getInsuranceId() : "Không có"));
            tvEhrEmergency.setText("SĐT Khẩn cấp: " + (patient.getEmergencyCallingNumber() != null ? patient.getEmergencyCallingNumber() : "Không có"));
        }

        // 2. --- PHẦN THÊM VÀO: Đổ dữ liệu vào Adapter thay vì dùng LayoutInflater ---
        List<MedicalVisits> historyList = ehrData.getMedicalHistory();

        medicalVisitsList.clear(); // Xóa dữ liệu cũ trong mảng

        if (historyList != null && !historyList.isEmpty()) {
            medicalVisitsList.addAll(historyList); // Nạp dữ liệu mới
            medicalAdapter.notifyDataSetChanged(); // Yêu cầu Adapter vẽ lại giao diện
            rvMedicalHistory.setVisibility(View.VISIBLE);
        } else {
            rvMedicalHistory.setVisibility(View.GONE);
        }
        // --------------------------------------------------------------------------

        // ĐÃ XÓA TOÀN BỘ PHẦN VÒNG LẶP FOR VÀ INFLATER CŨ Ở ĐÂY
    }

    // Đã chuyển hàm formatDateString sang bên class Adapter

    // Hàm tạo Mock Data giữ nguyên để test
    private EHR getMockEhrData() {
        Patient mockPatient = new Patient();
        mockPatient.setPatientId("BN-987654");
        mockPatient.setFullName("Nguyễn Văn Kiểm Thử");
        mockPatient.setBloodType("O+");
        mockPatient.setHeights(172.5);
        mockPatient.setWeights(68.0);
        mockPatient.setInsuranceId("DN401123456789");
        mockPatient.setEmergencyCallingNumber("0901 234 567 (Vợ)");

        java.util.List<MedicalVisits> mockHistory = new java.util.ArrayList<>();

        MedicalVisits visit1 = new MedicalVisits();
        visit1.setVisitId("V001");
        visit1.setVisitDate("2026-03-24T08:30:00.000Z"); // Chuẩn ISO 8601
        visit1.setHospital("Bệnh viện Đa khoa Quốc Tế");
        visit1.setDepartment("Khoa Hô hấp");
        visit1.setDoctorName("BS. CKII Trần Trọng Khang");
        visit1.setDiagnosis("Viêm phế quản cấp tính, theo dõi hen suyễn");

        List<Prescription> prescriptions1 = new ArrayList<>();
        prescriptions1.add(new Prescription(
                "P001", "Amoxicillin 500mg", "1 viên", "2 lần/ngày", 14,
                "Uống sau khi ăn no, không tự ý ngưng thuốc", "2026-03-24T09:00:00.000Z"
        ));
        prescriptions1.add(new Prescription(
                "P002", "Paracetamol 500mg", "1 viên", "Khi sốt > 38.5 độ", 10,
                "Cách nhau ít nhất 4-6 tiếng nếu còn sốt", "2026-03-24T09:00:00.000Z"
        ));
        visit1.setPrescriptions(prescriptions1);
        mockHistory.add(visit1);

        MedicalVisits visit2 = new MedicalVisits();
        visit2.setVisitId("V002");
        visit2.setVisitDate("2025-11-15T14:15:00.000Z");
        visit2.setHospital("Phòng khám Đa khoa Tâm Anh");
        visit2.setDepartment("Khoa Tiêu hóa");
        visit2.setDoctorName("BS. Lê Thị Lan");
        visit2.setDiagnosis("Trào ngược dạ dày thực quản (GERD)");

        List<Prescription> prescriptions2 = new ArrayList<>();
        prescriptions2.add(new Prescription(
                "P003", "Omeprazole 20mg", "1 viên", "1 lần/ngày", 30,
                "Uống trước bữa ăn sáng 30 phút", "2025-11-15T14:30:00.000Z"
        ));
        visit2.setPrescriptions(prescriptions2);
        mockHistory.add(visit2);

        EHR mockEhr = new EHR();
        mockEhr.setPatientInfo(mockPatient);
        mockEhr.setMedicalHistory(mockHistory);

        List<Prescription> allPrescriptions = new ArrayList<>();
        allPrescriptions.addAll(prescriptions1);
        allPrescriptions.addAll(prescriptions2);
        mockEhr.setAllPrescriptions(allPrescriptions);

        return mockEhr;
    }
}