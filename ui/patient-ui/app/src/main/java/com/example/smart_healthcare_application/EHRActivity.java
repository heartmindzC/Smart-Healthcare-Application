package com.example.smart_healthcare_application;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.MedicalHistoryAdapter;
import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.EHRRepository;
import com.example.smart_healthcare_application.models.EHR;
import com.example.smart_healthcare_application.models.MedicalVisits;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.Prescription;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;

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

        // fillEhrData(getMockEhrData()); // Comment mock data
        fetchEhrData(); // Gọi dữ liệu thực từ API
    }

    private void fetchEhrData() {
        User currentUser = LoginManager.getInstance(this).getUser();
        if (currentUser == null || currentUser.getUserId() == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        EHRRepository.getInstance().getEHRByUser(currentUser.getUserId(), new ApiCallback<EHR>() {
            @Override
            public void onSuccess(EHR result) {
                if (result != null) {
                    runOnUiThread(() -> {
                        fillEhrData(result);
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(EHRActivity.this, "Lỗi khi lấy hồ sơ bệnh án: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
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
        Patient patient = LoginManager.getInstance(EHRActivity.this).getPatient();
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

    // -------------------------------------------------------
    // Mock data (Đã được comment lại theo yêu cầu)
    // -------------------------------------------------------
    /*
    private EHR getMockEhrData() {
        ... (phần code mock cũ) ...
        return mockEhr;
    }
    */
}