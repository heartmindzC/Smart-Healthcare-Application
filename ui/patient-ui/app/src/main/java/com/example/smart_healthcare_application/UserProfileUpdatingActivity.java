package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.PatientRepository;
import com.example.smart_healthcare_application.api.repository.UserRepository;
import com.example.smart_healthcare_application.api.request.PatientRequest;
import com.example.smart_healthcare_application.api.request.UpdatePatientRequest;
import com.example.smart_healthcare_application.api.request.UpdateUserRequest;
import com.example.smart_healthcare_application.utils.LoginManager;
import com.google.android.material.textfield.TextInputEditText;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.models.Patient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserProfileUpdatingActivity extends AppCompatActivity {
    private TextInputEditText etName, etBirth, etEmail, etPhone, etAddress, etInsurance, etEmergencyPhone, etWeight, etHeight;
    private RadioGroup rgGender;
    private Spinner spBloodType;
    private Button btnSave;
    private ProgressBar progressBarBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_updating);

        initViews();
        setupBloodTypeSpinner();

        etBirth.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveProfileData());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        user =LoginManager.getInstance(UserProfileUpdatingActivity.this).getUser();

        PatientRepository.getInstance().getPatientByUserId
                (user.getUserId(), new ApiCallback<Patient>() {
            @Override
            public void onSuccess(Patient result) {
                if (result != null) {
                    fillData(user, result);
                }
                else {
                    Toast.makeText(UserProfileUpdatingActivity.this, "Đã xảy ra lỗi ở phía server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UserProfileUpdatingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etBirth = findViewById(R.id.etBirth);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etInsurance = findViewById(R.id.etInsurance);
        etEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        rgGender = findViewById(R.id.rgGender);
        spBloodType = findViewById(R.id.spBloodType);
        btnSave = findViewById(R.id.btnSaveProfile);
        progressBarBtn = findViewById(R.id.progressBarBtn);
    }

    private void setupBloodTypeSpinner() {
        String[] types = {"A", "B", "AB", "O", "Chưa rõ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spBloodType.setAdapter(adapter);
    }

    public void fillData(User user, Patient patient) {

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

        if (patient != null) {
            if (patient.getInsuranceId() != null) etInsurance.setText(patient.getInsuranceId());
            if (patient.getEmergencyCallingNumber() != null) etEmergencyPhone.setText(patient.getEmergencyCallingNumber());

            if (patient.getHeights() != null && patient.getHeights() > 0) {
                etHeight.setText(String.valueOf(patient.getHeights()));
            }
            if (patient.getWeights() != null && patient.getWeights() > 0) {
                etWeight.setText(String.valueOf(patient.getWeights()));
            }

            if (patient.getBloodType() != null) {
                selectBloodTypeSpinner(patient.getBloodType());
            }
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

    private void selectBloodTypeSpinner(String bloodType) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spBloodType.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equalsIgnoreCase(bloodType)) {
                    spBloodType.setSelection(i);
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
        String insurance = etInsurance.getText().toString().trim();
        String emergencyPhone = etEmergencyPhone.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Không được để trống tên");
            etName.requestFocus();
            return;
        }

        String gender = "";
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

        String bloodType = spBloodType.getSelectedItem().toString();

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

        UpdatePatientRequest patientRequest = new UpdatePatientRequest();
        patientRequest.setInsuranceId(insurance);
        patientRequest.setEmergencyCallingNumber(emergencyPhone);
        patientRequest.setBloodType(bloodType);

        try {
            if (!heightStr.isEmpty()) {
                patientRequest.setHeights(Double.parseDouble(heightStr));
            } else {
                patientRequest.setHeights(0.0);
            }

            if (!weightStr.isEmpty()) {
                patientRequest.setWeights(Double.parseDouble(weightStr));
            } else {
                patientRequest.setWeights(0.0);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Chiều cao hoặc cân nặng phải là số", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading(true);

        UserRepository.getInstance().updateUser(user.getUserId(), userRequest, new ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                updatePatient(patientRequest);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(UserProfileUpdatingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePatient(UpdatePatientRequest request) {
        PatientRepository.getInstance().updatePatient(user.getUserId(), request, new ApiCallback<Patient>() {
            @Override
            public void onSuccess(Patient result) {
                Toast.makeText(UserProfileUpdatingActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileUpdatingActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(UserProfileUpdatingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
