package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.UserRepository;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordChangingActivity extends AppCompatActivity {

    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnUpdatePassword;
    private ImageButton btnBack;
    private ProgressBar progressBarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_changing);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnBack = findViewById(R.id.btnBack);
        progressBarBtn = findViewById(R.id.progressBarBtn); // Ánh xạ ProgressBar
    }

    // Hàm "phép thuật" quản lý trạng thái nút bấm
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            // Ẩn chữ đi và hiện vòng xoay
            btnUpdatePassword.setText("");
            btnUpdatePassword.setEnabled(false); // Khóa nút
            progressBarBtn.setVisibility(View.VISIBLE);
        } else {
            // Hiện lại chữ và ẩn vòng xoay
            btnUpdatePassword.setText("CẬP NHẬT");
            btnUpdatePassword.setEnabled(true);
            progressBarBtn.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUpdatePassword.setOnClickListener(v -> {
            String currentPass = etCurrentPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            // Validate nhanh
            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                return;
            }

            // 1. Chuyển nút sang trạng thái đang tải
            showLoading(true);

            // 2. Gọi API cập nhật
            updatePasswordApi(currentPass, newPass);
        });
    }

    private void updatePasswordApi(String currentPass, String newPass) {
        User loginManager = LoginManager.getInstance(this).getUser();
        UserRepository userRepository = UserRepository.getInstance();
        Log.d("Update password", "Information: " + loginManager.getUserId() + " " + currentPass + " " + newPass);
        userRepository.changePassword(loginManager.getUserId(), currentPass, newPass, new ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    Intent intent = new Intent(PasswordChangingActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    showLoading(false);
                    Toast.makeText(PasswordChangingActivity.this, "Đã xảy ra lỗi, vui lòng thử lại. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(PasswordChangingActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}