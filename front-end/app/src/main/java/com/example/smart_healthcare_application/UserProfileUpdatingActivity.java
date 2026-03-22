package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class UserProfileUpdatingActivity extends AppCompatActivity {

    private TextInputEditText etName, etCCCD, etBirth, etEmail, etPhone, etAddress, etInsurance, etEmergencyPhone, etWeight, etHeight;
    private RadioGroup rgGender;
    private Spinner spBloodType;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_updating);

        initViews();
        setupBloodTypeSpinner();

        // Mở lịch khi nhấn vào ô ngày sinh
        etBirth.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveProfileData());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etCCCD = findViewById(R.id.etCCCD);
        etBirth = findViewById(R.id.etBirth);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etInsurance = findViewById(R.id.etInsurance);
        etEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        rgGender = findViewById(R.id.rgGender);
        spBloodType = findViewById(R.id.spBloodType);
        btnSave = findViewById(R.id.btnSaveProfile);
    }

    private void setupBloodTypeSpinner() {
        String[] types = {"A", "B", "AB", "O", "Chưa rõ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spBloodType.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            etBirth.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveProfileData() {
        // Kiểm tra validation đơn giản
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Không được để trống tên");
            return;
        }

        // Logic gửi dữ liệu lên Server/Firebase ở đây
        Toast.makeText(this, "Đang lưu thay đổi...", Toast.LENGTH_SHORT).show();
        finish();
    }
}