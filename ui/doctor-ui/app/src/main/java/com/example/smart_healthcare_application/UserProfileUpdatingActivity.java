package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.UserRepository;
import com.example.smart_healthcare_application.api.repository.DoctorRepository;
import com.example.smart_healthcare_application.api.request.UpdateUserRequest;
import com.example.smart_healthcare_application.api.request.UpdateDoctorRequest;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserProfileUpdatingActivity extends AppCompatActivity {
    private TextInputEditText etName, etBirth, etEmail, etPhone, etAddress;
    private TextInputEditText etHospital, etDepartment, etLicense;
    private RadioGroup rgGender;
    private Button btnSave;
    private ProgressBar progressBarBtn;
    User user;
    Doctor doctor;
    String gender = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_updating);

        initViews();

        etBirth.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveProfileData());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        user = LoginManager.getInstance(UserProfileUpdatingActivity.this).getUser();
        doctor = LoginManager.getInstance(UserProfileUpdatingActivity.this).getDoctor();
        if (user != null) {
             fillData(user, doctor);
        } else {
             Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
             finish();
        }
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etBirth = findViewById(R.id.etBirth);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etHospital = findViewById(R.id.etHospital);
        etDepartment = findViewById(R.id.etDepartment);
        etLicense = findViewById(R.id.etLicense);
        rgGender = findViewById(R.id.rgGender);
        btnSave = findViewById(R.id.btnSaveProfile);
        progressBarBtn = findViewById(R.id.progressBarBtn);
    }

    public void fillData(User user, Doctor doctor) {
        if (user != null) {
            if (user.getFullname() != null) etName.setText(user.getFullname());
            if (user.getEmail() != null) etEmail.setText(user.getEmail());
            if (user.getPhone() != null) etPhone.setText(user.getPhone());
            if (user.getAddress() != null) etAddress.setText(user.getAddress());

            if (user.getBirth() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etBirth.setText(sdf.format(user.getBirth()));
            }

            if (user.getGender() != null) {
                checkGenderRadioButton(user.getGender());
            }
        }
        
        if (doctor != null) {
            if (doctor.getHospitalId() != null) etHospital.setText(doctor.getHospitalId());
            if (doctor.getDepartment() != null) etDepartment.setText(doctor.getDepartment());
            if (doctor.getLicenseId() != null) etLicense.setText(doctor.getLicenseId());
        }
    }

    private void checkGenderRadioButton(String genderText) {
        if (genderText == null) return;

        String expectedUiText = "";
        if (genderText.equalsIgnoreCase("MALE")) {
            expectedUiText = "Nam";
        } else if (genderText.equalsIgnoreCase("FEMALE")) {
            expectedUiText = "Nữ";
        } else {
            expectedUiText = genderText;
        }

        for (int i = 0; i < rgGender.getChildCount(); i++) {
            View view = rgGender.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton rb = (RadioButton) view;
                if (rb.getText().toString().equalsIgnoreCase(expectedUiText)) {
                    rb.setChecked(true);
                    break;
                }
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
            etBirth.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnSave.setText("");
            btnSave.setEnabled(false);
            progressBarBtn.setVisibility(View.VISIBLE);
        } else {
            btnSave.setText("LƯU THÔNG TIN");
            btnSave.setEnabled(true);
            progressBarBtn.setVisibility(View.GONE);
        }
    }

    private void saveProfileData() {
        String name = etName.getText().toString().trim();
        String birthStr = etBirth.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Không được để trống tên");
            etName.requestFocus();
            return;
        }

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            String uiText = selectedRadioButton.getText().toString();

            if (uiText.equalsIgnoreCase("Nam")) {
                gender = "MALE";
            } else if (uiText.equalsIgnoreCase("Nữ")) {
                gender = "FEMALE";
            } else {
                gender = "OTHER";
            }
        }

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setFullname(name);
        userRequest.setEmail(email);
        userRequest.setPhone(phone);
        userRequest.setAddress(address);
        userRequest.setGender(gender);

        if (!birthStr.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                java.util.Date date = inputFormat.parse(birthStr);

                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

                String isoDateStr = isoFormat.format(date);

                userRequest.setBirth(isoDateStr);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi định dạng ngày sinh", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String hospitalId = etHospital.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String licenseId = etLicense.getText().toString().trim();

        showLoading(true);

        UserRepository.getInstance().updateUser(user.getUserId(), userRequest, new ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                LoginManager.getInstance(UserProfileUpdatingActivity.this).saveLoginSession(result);

                if (doctor == null || doctor.getDoctorId() == null) {
                    showLoading(false);
                    Toast.makeText(UserProfileUpdatingActivity.this, "Cập nhật User thành công nhưng không tìm thấy Doctor ID", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserProfileUpdatingActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                UpdateDoctorRequest doctorRequest = new UpdateDoctorRequest();
                doctorRequest.setHospitalId(hospitalId);
                doctorRequest.setDepartment(department);
                doctorRequest.setLicenseId(licenseId);
                if (userRequest.getBirth() != null) {
                    doctorRequest.setBirth(userRequest.getBirth());
                }
                doctorRequest.setFullName(name);
                doctorRequest.setGender(gender);

                DoctorRepository.getInstance().updateDoctor(doctor.getDoctorId(), doctorRequest, new ApiCallback<Doctor>() {
                    @Override
                    public void onSuccess(Doctor updatedDoctor) {
                        showLoading(false);
                        LoginManager.getInstance(UserProfileUpdatingActivity.this).saveDoctorSession(updatedDoctor);
                        Toast.makeText(UserProfileUpdatingActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserProfileUpdatingActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showLoading(false);
                        Toast.makeText(UserProfileUpdatingActivity.this, "Cập nhật thông tin Bác sĩ thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(UserProfileUpdatingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
