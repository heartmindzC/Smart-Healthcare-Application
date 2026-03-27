package com.example.smart_healthcare_application;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.AppointmentAdapter;
import com.example.smart_healthcare_application.adapters.OnCancelAppointmentListener;
import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.AppointmentRepository;
import com.example.smart_healthcare_application.models.Appointment;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppointmentHistoryActivity extends AppCompatActivity implements OnCancelAppointmentListener {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private AppointmentRepository appointmentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        appointmentRepository = new AppointmentRepository();
        initViews();
        // loadMockData();
        fetchAppointments();
    }

    private void initViews() {
        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        adapter.setCancelListener(this);
        rvAppointments.setAdapter(adapter);
    }

    private void fetchAppointments() {
        Patient currentPatient = LoginManager.getInstance(this).getPatient();
        if (currentPatient == null || currentPatient.getPatientId() == null) {
            Toast.makeText(this, "Không tìm thấy hồ sơ bệnh nhân. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        appointmentRepository.getAppointmentByUser(currentPatient.getPatientId(), new ApiCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                if (result != null) {
                    appointmentList = result;
                    
                    // Sắp xếp theo ngày hẹn
                    Collections.sort(appointmentList, (a1, a2) -> {
                        String date1 = a1.getAppointmentDateTime();
                        String date2 = a2.getAppointmentDateTime();
                        if (date1 == null) return 1;
                        if (date2 == null) return -1;
                        return date2.compareTo(date1);
                    });

                    runOnUiThread(() -> {
                        adapter.updateData(appointmentList);
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentHistoryActivity.this, "Lỗi khi lấy lịch hẹn: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onCancelAppointment(String appointmentId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy lịch")
                .setMessage("Bạn có chắc chắn muốn hủy lịch hẹn này không?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> {
                    cancelAppointment(appointmentId);
                })
                .setNegativeButton("Quay lại", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void cancelAppointment(String appointmentId) {
        appointmentRepository.cancelAppointment(appointmentId, new ApiCallback<Appointment>() {
            @Override
            public void onSuccess(Appointment result) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentHistoryActivity.this,
                            "Hủy lịch hẹn thành công!", Toast.LENGTH_SHORT).show();
                    for (Appointment appt : appointmentList) {
                        if (appt.getAppointmentId().equals(appointmentId)) {
                            appt.setStatus("CANCELLED");
                            break;
                        }
                    }
                    adapter.updateData(new ArrayList<>(appointmentList));
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentHistoryActivity.this,
                            "Hủy lịch thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    // -------------------------------------------------------
    // Mock data (Đã comment theo yêu cầu)
    // -------------------------------------------------------
    /*
    private void loadMockData() {
        List<Appointment> mockList = new ArrayList<>();

        // ... các phần mock data cũ ...
        adapter.updateData(mockList);
    }
    */
}