package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.UserRepository;
import com.example.smart_healthcare_application.models.User;

import java.util.Locale;

public class OtpVerificationActivity extends AppCompatActivity {

    private TextView tvTimer, tvResendOtp, tvOtpMessage;
    private EditText etOtpCode;
    private Button btnVerifyOtp;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBar;

    // 5 phút = 5 * 60 * 1000 = 300000 ms
    private static final long START_TIME_IN_MILLIS = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        tvTimer = findViewById(R.id.tvTimer);
        tvResendOtp = findViewById(R.id.tvResendOtp);
        tvOtpMessage = findViewById(R.id.tvOtpMessage);
        etOtpCode = findViewById(R.id.etOtpCode);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBar = findViewById(R.id.progressBar);

        // Lấy email từ màn hình trước truyền sang để hiển thị cho thân thiện
        String email = getIntent().getStringExtra("EMAIL_EXTRA");
        if (email != null) {
            tvOtpMessage.setText("Mã OTP đã được gửi tới:\n" + email);
        }

        // Bắt đầu đếm ngược ngay khi mở màn hình
        startTimer();

        // Xử lý nút Xác nhận OTP
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                String otp = etOtpCode.getText().toString().trim();
                if (otp.length() == 6) {
                    validateOTP(email, otp);
                } else {
                    etOtpCode.setError("Vui lòng nhập đủ 6 số OTP");
                }
            }
        });

        // Xử lý nút Gửi lại mã (Ban đầu bị vô hiệu hóa)
        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OtpVerificationActivity.this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
                startTimer();
            }
        });
    }

    private void validateOTP(String email, String otp) {
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.verifyOTP(email, otp, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    showLoading(false);
                    Intent intent = new Intent(OtpVerificationActivity.this, PasswordRecoveryActivity.class);
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                    finish();
                }
                else {
                    showLoading(false);
                    Toast.makeText(OtpVerificationActivity.this, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                etOtpCode.setError(errorMessage);
                showLoading(false);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnVerifyOtp.setEnabled(false);
            btnVerifyOtp.setText("Đang xác nhận...");
            // Vô hiệu hóa cả nút gửi lại mã và ô nhập liệu trong lúc chờ
            tvResendOtp.setEnabled(false);
            etOtpCode.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnVerifyOtp.setEnabled(true);
            btnVerifyOtp.setText("Xác Nhận OTP");
            // Mở khóa lại
            tvResendOtp.setEnabled(true);
            etOtpCode.setEnabled(true);
        }
    }

    private void startTimer() {
        tvResendOtp.setEnabled(false);
        tvResendOtp.setTextColor(getResources().getColor(android.R.color.darker_gray));

        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvTimer.setText("Thời gian còn lại: " + timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Mã OTP đã hết hạn");
                // Cho phép bấm Gửi lại mã
                tvResendOtp.setEnabled(true);
                tvResendOtp.setTextColor(getResources().getColor(android.R.color.holo_blue_light)); // Chuyển sang màu xanh
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}