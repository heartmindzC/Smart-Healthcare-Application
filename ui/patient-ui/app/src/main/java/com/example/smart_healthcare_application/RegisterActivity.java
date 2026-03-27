package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.PatientRepository;
import com.example.smart_healthcare_application.api.repository.UserRepository;
import com.example.smart_healthcare_application.api.request.PatientRequest;
import com.example.smart_healthcare_application.api.request.RegisterRequest;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.util.Calendar;
import java.util.Locale;

// Nhớ import các class Model của bạn tại đây
// import com.example.smart_healthcare_application.model.RegisterRequest;
// import com.example.smart_healthcare_application.model.PatientRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullname, etUserId, etPhone, etEmergencyPhone, etEmail, etAddress,
            etJob, etHealthInsurance, etBloodType, etWeight, etHeight, etPassword;
    private TextView tvBirthDate, tvBackToLogin;
    private RadioGroup rgGender;
    private Button btnSubmitRegister;
    private String selectedDate = ""; // Biến lưu ngày sinh chuẩn ISO 8601
    private ProgressBar progressBarRegister;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ View
        initViews();

        // Xử lý chọn Ngày Sinh
        tvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Xử lý nút Đăng Ký
        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    showLoading(true);
                    // --- BẮT ĐẦU MAP DỮ LIỆU ---

                    // 1. Trích xuất các chuỗi văn bản (đã loại bỏ khoảng trắng thừa)
                    String fullname = etFullname.getText().toString().trim();
                    String userId = etUserId.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    String emergencyPhone = etEmergencyPhone.getText().toString().trim();
                    String job = etJob.getText().toString().trim();
                    String insuranceId = etHealthInsurance.getText().toString().trim();
                    String bloodType = etBloodType.getText().toString().trim();

                    // 2. Ép kiểu dữ liệu số (đã check rỗng trong validateInput)
                    Double weight = Double.parseDouble(etWeight.getText().toString().trim());
                    Double height = Double.parseDouble(etHeight.getText().toString().trim());

                    // 3. Xử lý logic Giới tính
                    String gender = (rgGender.getCheckedRadioButtonId() == R.id.rbMale) ? "MALE" : "FEMALE";

                    // 4. Khởi tạo và gán giá trị cho RegisterRequest
                    RegisterRequest registerReq = new RegisterRequest();
                    registerReq.setUserId(userId);
                    registerReq.setPassword(password);
                    registerReq.setPhone(phone);
                    registerReq.setEmail(email);
                    registerReq.setFullname(fullname);
                    registerReq.setAddress(address);
                    registerReq.setBirth(selectedDate);
                    registerReq.setGender(gender);

                    // 5. Khởi tạo và gán giá trị cho PatientRequest
                    PatientRequest patientReq = new PatientRequest();
                    patientReq.setUserId(userId);
                    patientReq.setFullName(fullname);
                    patientReq.setBirth(selectedDate);
                    patientReq.setGender(gender);
                    patientReq.setInsuranceId(insuranceId);
                    patientReq.setEmergencyCallingNumber(emergencyPhone);
                    patientReq.setJob(job);
                    patientReq.setBloodType(bloodType);
                    patientReq.setHeights(height);
                    patientReq.setWeights(weight);

                    // --- KẾT THÚC MAP DỮ LIỆU ---

                    callApi(registerReq, patientReq);

//                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển vào màn hình chính
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }
            }
        });

        // Xử lý nút Quay lại Đăng Nhập
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callApi(RegisterRequest registerReq, PatientRequest patientReq) {
        Log.d("Register", "Create user ...");
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.register(registerReq, new ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Log.d("Register", "Create user success");
                callAPiPatient(patientReq);
                user = result;
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Register", "Create user failed: " + errorMessage);
                showLoading(false);
            }
        });
    }

    private void callAPiPatient(PatientRequest patientRequest) {
        Log.d("Register", "Create patient ...");
        PatientRepository patientRepository = PatientRepository.getInstance();
        patientRepository.createPatient(patientRequest, new ApiCallback<Patient>() {
            @Override
            public void onSuccess(Patient result) {
                Log.d("Register", "Create patient success");
                LoginManager.getInstance(RegisterActivity.this).saveLoginSession(user);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Register", "Create patient failed: " + errorMessage);
                showLoading(false);
            }
        });
    }

    private void initViews() {
        etFullname = findViewById(R.id.etFullname);
        etUserId = findViewById(R.id.etUserId);
        etPhone = findViewById(R.id.etPhone);
        etEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etJob = findViewById(R.id.etJob);
        etHealthInsurance = findViewById(R.id.etHealthInsurance);
        etBloodType = findViewById(R.id.etBloodType);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etPassword = findViewById(R.id.etPassword);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        rgGender = findViewById(R.id.rgGender);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBarRegister = findViewById(R.id.progressBarRegister);
    }

    // Hàm hiển thị Lịch chọn ngày
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Sửa lại format ngày sinh để xuất ra đúng chuẩn ISO 8601 như backend yêu cầu
                        // VD: 2026-03-22T00:00:00.000Z
                        selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02dT00:00:00.000Z", year, (monthOfYear + 1), dayOfMonth);
                        tvBirthDate.setText(String.format(Locale.getDefault(), "Ngày sinh: %02d/%02d/%04d", dayOfMonth, (monthOfYear + 1), year));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Hàm kiểm tra tính hợp lệ của dữ liệu (Client-side validation)
    private boolean validateInput() {
        String fullname = etFullname.getText().toString().trim();
        String userId = etUserId.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String emergencyPhone = etEmergencyPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            etFullname.setError("EMPTY_NAME");
            return false;
        }
        if (!userId.matches("^\\d{12}$")) {
            etUserId.setError("IDENTITY_NUMBER_INVALID (Yêu cầu 12 số)");
            return false;
        }
        if (!phone.matches("^\\d{10}$")) {
            etPhone.setError("PHONE_INVALID (Yêu cầu 10 số)");
            return false;
        }

        if (!emergencyPhone.matches("^\\d{10}$")) {
            etEmergencyPhone.setError("Yêu cầu 10 số");
            return false;
        }
        if (emergencyPhone.equals(phone)) {
            etEmergencyPhone.setError("SĐT khẩn cấp phải khác SĐT của bạn");
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("EMAIL_INVALID");
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError("EMPTY_ADDRESS");
            return false;
        }

        if (weightStr.isEmpty()) {
            etWeight.setError("Vui lòng nhập cân nặng");
            return false;
        }
        if (heightStr.isEmpty()) {
            etHeight.setError("Vui lòng nhập chiều cao");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("EMPTY_PASSWORD");
            return false;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "NULL_DATE: Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "NULL_GENDER: Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnSubmitRegister.setText(""); // Xóa chữ trên nút
            btnSubmitRegister.setEnabled(false); // Vô hiệu hóa nút để tránh click nhiều lần
            progressBarRegister.setVisibility(View.VISIBLE); // Hiện vòng xoay
        } else {
            btnSubmitRegister.setText("Đăng Ký & Vào App"); // Trả lại chữ
            btnSubmitRegister.setEnabled(true); // Mở khóa nút
            progressBarRegister.setVisibility(View.GONE); // Ẩn vòng xoay
        }
    }
}