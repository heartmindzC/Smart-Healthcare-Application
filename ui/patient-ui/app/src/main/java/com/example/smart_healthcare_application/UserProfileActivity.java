package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button; // Đã thêm import cho Button
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.PatientRepository;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {

    TextView btnChangePassword;
    ImageButton btnEditProfile;
    TextView tvFullName, tvUserId;
    TextView tvPhone, tvEmail, tvAddress;
    TextView tvCCCD, tvBirth, tvGender, tvRole;
    TextView tvHealthInsurance, tvHeight, tvWeight, tvBloodType;
    Button btnLogout;
    User user;
    Patient patient;

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
        if (user == null) {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            LoginManager.getInstance(UserProfileActivity.this).logout();
            startActivity(intent);
            finish();
        }

        loadPatient(user.getUserId());
        fillUserData(user);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, PasswordChangingActivity.class);
                startActivity(intent);
            }
        });

        btnEditProfile = findViewById(R.id.btnEditProfile);
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

    private void fillPatientData(Patient patient) {
        if (patient == null) return;

        if (patient.getInsuranceId() != null && !patient.getInsuranceId().isEmpty()) {
            tvHealthInsurance.setText("BHYT: " + patient.getInsuranceId());
        } else {
            tvHealthInsurance.setText("BHYT: Chưa cập nhật");
        }

        if (patient.getHeights() != null && patient.getHeights() > 0) {
            tvHeight.setText("Cao: " + patient.getHeights() + " cm");
        } else {
            tvHeight.setText("Cao: ...");
        }

        if (patient.getWeights() != null && patient.getWeights() > 0) {
            tvWeight.setText("Nặng: " + patient.getWeights() + " kg");
        } else {
            tvWeight.setText("Nặng: ...");
        }

        if (patient.getBloodType() != null && !patient.getBloodType().isEmpty()) {
            tvBloodType.setText("Nhóm máu: " + patient.getBloodType());
        } else {
            tvBloodType.setText("Nhóm máu: Chưa xác định");
        }
    }

    private void loadPatient(String userId) {
        PatientRepository.getInstance().getPatientByUserId(userId, new ApiCallback<Patient>() {
            @Override
            public void onSuccess(Patient result) {
                if (result != null) {
                    patient = result;
                    fillPatientData(patient);
                }
                else {
                    Toast.makeText(UserProfileActivity.this, "Đã xảy ra lỗi ở phía server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvUserId = findViewById(R.id.tvUserId);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvCCCD = findViewById(R.id.tvCCCD);
        tvBirth = findViewById(R.id.tvBirth);
        tvGender = findViewById(R.id.tvGender);
        tvRole = findViewById(R.id.tvRole);
        tvHealthInsurance = findViewById(R.id.tvHealthInsurance);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvBloodType = findViewById(R.id.tvBloodType);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }

    private void fillUserData(User user) {
        if (user == null) return;
        tvFullName.setText(user.getFullname() != null ? user.getFullname() : "Chưa cập nhật");
        tvUserId.setText(user.getUserId() != null ? "ID: " + user.getUserId() : "ID: ...");

        tvPhone.setText(user.getPhone() != null ? user.getPhone() : "Chưa có SĐT");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa có Email");
        tvAddress.setText(user.getAddress() != null ? user.getAddress() : "Chưa cập nhật địa chỉ");

        tvGender.setText(user.getGender() != null ? "Giới tính: " + user.getGender() : "Giới tính: Chưa cập nhật");

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

        tvCCCD.setText("CCCD: " + user.getUserId());
    }
}