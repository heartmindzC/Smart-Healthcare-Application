package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.UserRepository;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etForgotEmail;
    Button btnSendOtp;
    TextView tvBack;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etForgotEmail = findViewById(R.id.etForgotEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        tvBack = findViewById(R.id.tvBackToLoginFromForgot);
        progressBar = findViewById(R.id.progressBar);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                String email = etForgotEmail.getText().toString().trim();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.contains("@gmail.com")) {
                    etForgotEmail.setError("Email không hợp lệ");
                    return;
                }

                UserRepository userRepository = UserRepository.getInstance();
                userRepository.forgotPassword(email, new ApiCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            showLoading(false);
                            Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                            intent.putExtra("EMAIL_EXTRA", email);
                            startActivity(intent);
                        }
                        else {
                            showLoading(false);
                            Toast.makeText(ForgotPasswordActivity.this, "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        etForgotEmail.setError(errorMessage);
                        showLoading(false);
                    }
                });



            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnSendOtp.setEnabled(false);
            btnSendOtp.setText("Đang gửi...");
        } else {
            progressBar.setVisibility(View.GONE);
            btnSendOtp.setEnabled(true);
            btnSendOtp.setText("Gửi Mã OTP");
        }
    }
}