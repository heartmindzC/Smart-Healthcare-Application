package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullname, etUserId, etPhone, etEmail, etAddress, etPassword;
    private TextView tvBirthDate, tvBackToLogin;
    private RadioGroup rgGender;
    private Button btnSubmitRegister;
    private String selectedDate = ""; // Biến lưu ngày sinh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ View
        etFullname = findViewById(R.id.etFullname);
        etUserId = findViewById(R.id.etUserId);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        rgGender = findViewById(R.id.rgGender);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Xử lý chọn Ngày Sinh
        tvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Xử lý nút Đăng Ký
        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // TODO: Gửi dữ liệu này qua API cho backend Spring Boot
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển vào màn hình chính
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        // Xử lý nút Quay lại Đăng Nhập
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng màn hình hiện tại để quay về Login
                finish();
            }
        });
    }

    // Hàm hiển thị Lịch chọn ngày
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        tvBirthDate.setText("Ngày sinh: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Hàm kiểm tra tính hợp lệ của dữ liệu (Client-side validation)
    private boolean validateInput() {
        String fullname = etFullname.getText().toString().trim();
        String userId = etUserId.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            etFullname.setError("EMPTY_NAME");
            return false;
        }
        if (!userId.matches("^\\d{12}$")) {
            etUserId.setError("IDENTITY_NUMBER_INVALID (Yêu cầu 12 số)");
            return false;
        }
        if (!phone.matches("^\\d{10}$")) {
            etPhone.setError("PHONE_INVALID (Yêu cầu 10 số)");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("EMAIL_INVALID");
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError("EMPTY_ADDRESS");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("EMPTY_PASSWORD");
            return false;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "NULL_DATE: Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "NULL_GENDER: Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}