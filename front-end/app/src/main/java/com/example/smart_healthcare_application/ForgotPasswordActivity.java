package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText etForgotEmail = findViewById(R.id.etForgotEmail);
        Button btnSendOtp = findViewById(R.id.btnSendOtp);
        TextView tvBack = findViewById(R.id.tvBackToLoginFromForgot);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getText().toString().trim();

                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etForgotEmail.setError("Email không hợp lệ");
                    return;
                }

                // Chuyển sang trang OTP và truyền email theo để hiển thị
                Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                intent.putExtra("EMAIL_EXTRA", email);
                startActivity(intent);
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại trang đăng nhập
            }
        });
    }
}