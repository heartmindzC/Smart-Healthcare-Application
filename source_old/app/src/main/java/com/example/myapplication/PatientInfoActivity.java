package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.request.PatientRequest;
import com.example.myapplication.api.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientInfoActivity extends AppCompatActivity {
    private final String LOG_TAG = "PatientInfoActivity:patient_service";
    
    private EditText etInsuranceId, etEmergencyCallingNumber, etJob, etHeights, etWeights;
    private Spinner spinnerBloodType;
    private Button btSubmit;
    
    private String userId, fullName, birth, gender;
    private boolean isSubmitting = false; // Flag để tránh double submit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Lấy thông tin user từ Intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        fullName = intent.getStringExtra("fullName");
        birth = intent.getStringExtra("birth");
        gender = intent.getStringExtra("gender");
        
        // Kiểm tra dữ liệu từ Intent
        if (userId == null || fullName == null || birth == null || gender == null) {
            Toast.makeText(this, "Missing user information. Please register again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo views
        etInsuranceId = findViewById(R.id.etInsuranceId);
        etEmergencyCallingNumber = findViewById(R.id.etEmergencyCallingNumber);
        etJob = findViewById(R.id.etJob);
        etHeights = findViewById(R.id.etHeights);
        etWeights = findViewById(R.id.etWeights);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        btSubmit = findViewById(R.id.btSubmit);
        
        // Setup spinner cho blood type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.blood_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);
        
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubmitting) {
                    Toast.makeText(PatientInfoActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (validateInput()) {
                    createPatient();
                }
            }
        });
    }
    
    private boolean validateInput() {
        String insuranceId = etInsuranceId.getText().toString().trim();
        String emergencyCallingNumber = etEmergencyCallingNumber.getText().toString().trim();
        String job = etJob.getText().toString().trim();
        String heightsStr = etHeights.getText().toString().trim();
        String weightsStr = etWeights.getText().toString().trim();
        
        if (insuranceId.isEmpty()) {
            Toast.makeText(this, "Please enter insurance ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (emergencyCallingNumber.isEmpty() || !emergencyCallingNumber.matches("[0-9]{10}")) {
            Toast.makeText(this, "Please enter valid emergency calling number (10 digits)", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (job.isEmpty()) {
            Toast.makeText(this, "Please enter your job", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (heightsStr.isEmpty()) {
            Toast.makeText(this, "Please enter your height", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (weightsStr.isEmpty()) {
            Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        try {
            Double.parseDouble(heightsStr);
            Double.parseDouble(weightsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Height and weight must be valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private void createPatient() {
        // Kiểm tra xem đang submit chưa
        if (isSubmitting) {
            Log.w(LOG_TAG, "Already submitting, ignoring duplicate request");
            return;
        }
        
        try {
            // Set flag để tránh double submit
            isSubmitting = true;
            btSubmit.setEnabled(false);
            btSubmit.setText("Submitting...");
            
            String insuranceId = etInsuranceId.getText().toString().trim();
            String emergencyCallingNumber = etEmergencyCallingNumber.getText().toString().trim();
            String job = etJob.getText().toString().trim();
            
            // Parse heights và weights với error handling
            Double heights;
            Double weights;
            try {
                heights = Double.parseDouble(etHeights.getText().toString().trim());
                weights = Double.parseDouble(etWeights.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Height and weight must be valid numbers", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error parsing numbers", e);
                isSubmitting = false;
                btSubmit.setEnabled(true);
                btSubmit.setText("Submit");
                return;
            }
            
            // Lấy blood type từ spinner
            Object selectedItem = spinnerBloodType.getSelectedItem();
            String bloodType = selectedItem != null ? selectedItem.toString() : "A";
            
            // Kiểm tra null values
            if (userId == null || fullName == null || birth == null || gender == null) {
                Toast.makeText(this, "Missing user information", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Missing user info - userId: " + userId + ", fullName: " + fullName);
                isSubmitting = false;
                btSubmit.setEnabled(true);
                btSubmit.setText("Submit");
                return;
            }
            
            PatientRequest patientRequest = new PatientRequest(
                    userId,
                    fullName,
                    birth,
                    gender,
                    insuranceId,
                    emergencyCallingNumber,
                    job,
                    bloodType,
                    heights,
                    weights
            );
            
            Log.d(LOG_TAG, "Creating patient with userId: " + userId);
            
            // Sử dụng patient service client
            ApiService api = ApiClient.getPatientServiceClient().create(ApiService.class);
            Call<ApiResponse> call = api.createPatient(patientRequest);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    // Reset flag và button
                    isSubmitting = false;
                    btSubmit.setEnabled(true);
                    btSubmit.setText("Submit");
                    
                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.isStatus()) {
                            Toast.makeText(PatientInfoActivity.this, 
                                    "Patient information saved successfully!", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Success: " + (apiResponse.getMessage() != null ? apiResponse.getMessage() : ""));
                            // Chuyển vào trang home
                            changeScreen(HomeAcitivity.class);
                        } else {
                            // Response có status = false
                            String errorMsg = "Failed to create patient";
                            if (apiResponse != null) {
                                if (apiResponse.getMessage() != null && !apiResponse.getMessage().trim().isEmpty()) {
                                    errorMsg = apiResponse.getMessage();
                                }
                                Log.e(LOG_TAG, "Error response - status: " + apiResponse.isStatus() + ", message: " + apiResponse.getMessage());
                            } else {
                                Log.e(LOG_TAG, "Error response - apiResponse is null");
                            }
                            Toast.makeText(PatientInfoActivity.this, 
                                    errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý error response
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "Error reading error body", e);
                        }
                        String errorMsg = "Error creating patient: " + response.code();
                        Toast.makeText(PatientInfoActivity.this, 
                                errorMsg, Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Error code: " + response.code() + ", body: " + errorBody);
                    }
                }
                
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    // Reset flag và button
                    isSubmitting = false;
                    btSubmit.setEnabled(true);
                    btSubmit.setText("Submit");
                    
                    String errorMsg = "Network error";
                    if (t != null && t.getMessage() != null && !t.getMessage().trim().isEmpty()) {
                        errorMsg = "Network error: " + t.getMessage();
                    }
                    Toast.makeText(PatientInfoActivity.this, 
                            errorMsg, 
                            Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Network error", t);
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            // Reset flag và button
            isSubmitting = false;
            btSubmit.setEnabled(true);
            btSubmit.setText("Submit");
            
            String errorMsg = "An unexpected error occurred";
            if (e != null && e.getMessage() != null && !e.getMessage().trim().isEmpty()) {
                errorMsg = "Error: " + e.getMessage();
            }
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "Unexpected error in createPatient", e);
            if (e != null) {
                e.printStackTrace();
            }
        }
    }
    
    private void changeScreen(Class<?> des) {
        Intent intent = new Intent(this, des);
        startActivity(intent);
        finish();
    }
}

