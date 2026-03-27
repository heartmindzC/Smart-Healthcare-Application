package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {

    Button btnChangePassword;
    Button btnEditProfile;
    TextView tvFullName, tvUserId;
    TextView tvPhone, tvEmail, tvAddress;
    TextView tvBirth, tvGender, tvRole;
    TextView tvHospital, tvDepartment, tvLicense;
    Button btnLogout;
    User user;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        user = LoginManager.getInstance(UserProfileActivity.this).getUser();
        doctor = LoginManager.getInstance(UserProfileActivity.this).getDoctor();
        if (user == null) {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            LoginManager.getInstance(UserProfileActivity.this).logout();
            startActivity(intent);
            finish();
            return;
        }

        fillUserData(user);
        fillDoctorData(doctor);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, PasswordChangingActivity.class);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserProfileUpdatingActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance(UserProfileActivity.this).logout();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvRole = findViewById(R.id.tvRole);
        tvUserId = findViewById(R.id.tvUserId);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvBirth = findViewById(R.id.tvBirth);
        tvGender = findViewById(R.id.tvGender);
        tvHospital = findViewById(R.id.tvHospital);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvLicense = findViewById(R.id.tvLicense);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnEditProfile = findViewById(R.id.btnEditProfile);
    }

    private void fillUserData(User user) {
        tvFullName.setText(user.getFullname() != null ? user.getFullname() : "Chưa cập nhật");
        tvUserId.setText(user.getUserId() != null ? "CCCD/ID: " + user.getUserId() : "CCCD/ID: ...");

        tvPhone.setText(user.getPhone() != null ? "SĐT: " + user.getPhone() : "SĐT: Chưa có");
        tvEmail.setText(user.getEmail() != null ? "Email: " + user.getEmail() : "Email: Chưa có");
        tvAddress.setText(user.getAddress() != null ? "Địa chỉ: " + user.getAddress() : "Địa chỉ: Chưa cập nhật");

        String genderText = "Chưa cập nhật";
        if (user.getGender() != null) {
             if (user.getGender().equalsIgnoreCase("MALE")) genderText = "Nam";
             else if (user.getGender().equalsIgnoreCase("FEMALE")) genderText = "Nữ";
             else genderText = user.getGender();
        }
        tvGender.setText("Giới tính: " + genderText);

        if (user.getBirth() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvBirth.setText("Ngày sinh: " + sdf.format(user.getBirth()));
        } else {
            tvBirth.setText("Ngày sinh: Chưa cập nhật");
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            String roleString = TextUtils.join(", ", user.getRoles());
            tvRole.setText("Vai trò: " + roleString);
        } else {
            tvRole.setText("Vai trò: Chưa xác định");
        }
    }

    private void fillDoctorData(Doctor doctor) {
        if (doctor == null) {
            tvHospital.setText("Bệnh viện: Chưa cập nhật");
            tvDepartment.setText("Chuyên khoa: Chưa cập nhật");
            tvLicense.setText("Chứng chỉ hành nghề: Chưa cập nhật");
            return;
        }
        
        tvHospital.setText(doctor.getHospitalId() != null && !doctor.getHospitalId().isEmpty() ? "Bệnh viện: " + doctor.getHospitalId() : "Bệnh viện: Chưa cập nhật");
        tvDepartment.setText(doctor.getDepartment() != null && !doctor.getDepartment().isEmpty() ? "Chuyên khoa: " + doctor.getDepartment() : "Chuyên khoa: Chưa cập nhật");
        tvLicense.setText(doctor.getLicenseId() != null && !doctor.getLicenseId().isEmpty() ? "Chứng chỉ hành nghề: " + doctor.getLicenseId() : "Chứng chỉ hành nghề: Chưa cập nhật");
    }
}
