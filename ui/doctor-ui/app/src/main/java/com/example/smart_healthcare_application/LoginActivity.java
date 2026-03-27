package com.example.smart_healthcare_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.api.repository.DoctorRepository;
import com.example.smart_healthcare_application.utils.LoginManager;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBarLogin;
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvForgotPassword;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");

        if (!savedUsername.isEmpty()) {
            etUsername.setText(savedUsername);
        }

        // Xử lý sự kiện click Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });



        // Xử lý sự kiện Quên mật khẩu
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initViews() {
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword  =findViewById(R.id.etPassword);
    }

    public void login(String username, String password) {
        Log.d("Login", "Login...");
        showLoading(true);
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.login(username, password, new ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Log.d("Login", "Login success !!!");
                LoginManager.getInstance(LoginActivity.this).saveLoginSession(result);

                // ghi nho dang nhap
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_USERNAME, username);
                editor.apply();

                // Lấy thông tin Bác sĩ
                DoctorRepository.getInstance().getDoctorByUserId(result.getUserId(), new ApiCallback<Doctor>() {
                    @Override
                    public void onSuccess(Doctor doctor) {
                        LoginManager.getInstance(LoginActivity.this).saveDoctorSession(doctor);
                        proceedToMain();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Login", "Get doctor info failed: " + errorMessage);
                        // Vẫn cho đăng nhập nhưng báo lỗi
                        Toast.makeText(LoginActivity.this, "Cảnh báo: Không thể tải thông tin Bác sĩ", Toast.LENGTH_SHORT).show();
                        proceedToMain();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("Login", "Login failed: " + errorMessage);
                showLoading(false);
            }
        });
    }

    private void proceedToMain() {
        showLoading(false);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnLogin.setText(""); // Xóa chữ để nhường chỗ cho vòng xoay
            btnLogin.setEnabled(false); // Vô hiệu hóa nút để tránh click nhiều lần
            progressBarLogin.setVisibility(View.VISIBLE); // Hiện vòng xoay
        } else {
            btnLogin.setText("Đăng Nhập"); // Trả lại chữ ban đầu
            btnLogin.setEnabled(true); // Mở khóa nút
            progressBarLogin.setVisibility(View.GONE); // Ẩn vòng xoay
        }
    }
}