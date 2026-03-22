package com.example.smart_healthcare_application;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class OtpVerificationActivity extends AppCompatActivity {

    private TextView tvTimer, tvResendOtp, tvOtpMessage;
    private EditText etOtpCode;
    private Button btnVerifyOtp;
    private CountDownTimer countDownTimer;

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
                String otp = etOtpCode.getText().toString().trim();
                if (otp.length() == 6) {
                    // TODO: Gửi OTP lên server để kiểm tra
                    Toast.makeText(OtpVerificationActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình Đặt lại mật k121hẩu mới (Tạo thêm ResetPasswordActivity nếu cần)

                } else {
                    etOtpCode.setError("Vui lòng nhập đủ 6 số OTP");
                }
            }
        });

        // Xử lý nút Gửi lại mã (Ban đầu bị vô hiệu hóa)
        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Gọi API gửi lại email ở đây
                Toast.makeText(OtpVerificationActivity.this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
                startTimer(); // Chạy lại bộ đếm
            }
        });
    }

    private void startTimer() {
        // Vô hiệu hóa nút gửi lại khi đang đếm ngược
        tvResendOtp.setEnabled(false);
        tvResendOtp.setTextColor(getResources().getColor(android.R.color.darker_gray));

        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Định dạng thời gian hiển thị dạng MM:SS
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
        // Hủy timer để tránh rò rỉ bộ nhớ (memory leak) khi thoát màn hình
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}