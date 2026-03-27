package com.example.smart_healthcare_application; // Hãy sửa lại đúng package của bạn

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.UserRepository;

public class PasswordRecoveryActivity extends AppCompatActivity {
    private TextView tvDisplayEmail;
    private EditText edtNewPassword, edtConfirmPassword;
    private Button btnSubmitReset;
    private String userEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        initView();
        getIntentData();
        setupListener();
    }

    private void initView() {
        tvDisplayEmail = findViewById(R.id.tvDisplayEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSubmitReset = findViewById(R.id.btnSubmitReset);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_EMAIL")) {
            userEmail = intent.getStringExtra("USER_EMAIL");

            tvDisplayEmail.setText("Đang đổi mật khẩu cho tài khoản:\n" + userEmail);
        } else {
            Toast.makeText(this, "Lỗi dữ liệu: Không tìm thấy Email", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmitReset.setEnabled(false);
            btnSubmitReset.setText("Đang cập nhật...");
            edtNewPassword.setEnabled(false);
            edtConfirmPassword.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSubmitReset.setEnabled(true);
            btnSubmitReset.setText("Cập nhật mật khẩu");
            edtNewPassword.setEnabled(true);
            edtConfirmPassword.setEnabled(true);
        }
    }

    private void setupListener() {
        btnSubmitReset.setOnClickListener(v -> {
            showLoading(true);
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            if (newPassword.isEmpty() || newPassword.length() < 6) {
                edtNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                edtNewPassword.requestFocus();
                showLoading(false);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                edtConfirmPassword.setError("Mật khẩu xác nhận không trùng khớp");
                edtConfirmPassword.requestFocus();
                showLoading(false);
                return;
            }

            updatePasswordApi(userEmail, newPassword);
        });
    }

    private void updatePasswordApi(String email, String password) {
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.resetPassword(email, password, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    showLoading(false);
                    Intent intent = new Intent(PasswordRecoveryActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(PasswordRecoveryActivity.this, "Thay đổi mật khẩu không thành công, vui lòng thử lại. ", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PasswordRecoveryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                showLoading(false);
            }
        });
    }
}