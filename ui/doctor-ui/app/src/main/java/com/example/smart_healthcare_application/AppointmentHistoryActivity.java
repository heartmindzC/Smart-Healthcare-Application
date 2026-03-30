package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.AppointmentAdapter;
import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.AppointmentRepository;
import com.example.smart_healthcare_application.factory.ConfirmAppointmentUI;
import com.example.smart_healthcare_application.models.Appointment;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentHistoryActivity extends AppCompatActivity implements ConfirmAppointmentUI.OnAppointmentActionListener {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<Appointment> allAppointments = new ArrayList<>();
    private List<Appointment> displayList = new ArrayList<>();
    
    private TextView tvFilterDate;
    private ImageView btnClearFilter;
    private String selectedDateStr = "";
    private Doctor currentDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        currentDoctor = LoginManager.getInstance(this).getDoctor();
        if (currentDoctor == null) {
            Toast.makeText(this, "Không tìm thấy thông tin bác sĩ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Mặc định lọc theo ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        selectedDateStr = String.format(Locale.getDefault(), "%02d/%02d/%d", 
                calendar.get(Calendar.DAY_OF_MONTH), 
                (calendar.get(Calendar.MONTH) + 1), 
                calendar.get(Calendar.YEAR));

        initViews();
        loadWorkSchedule();
    }

    private void initViews() {
        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new AppointmentAdapter(displayList, this);
        rvAppointments.setAdapter(adapter);

        tvFilterDate = findViewById(R.id.tvFilterDate);
        btnClearFilter = findViewById(R.id.btnClearFilter);

        // Hiển thị ngày mặc định
        tvFilterDate.setText(selectedDateStr);
        btnClearFilter.setVisibility(View.VISIBLE);

        tvFilterDate.setOnClickListener(v -> showDatePicker());
        btnClearFilter.setOnClickListener(v -> clearFilter());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDateStr = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
            tvFilterDate.setText(selectedDateStr);
            btnClearFilter.setVisibility(View.VISIBLE);
            applyFilter();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void clearFilter() {
        selectedDateStr = "";
        tvFilterDate.setText("Chọn ngày");
        btnClearFilter.setVisibility(View.GONE);
        applyFilter();
    }

    private void loadWorkSchedule() {
        AppointmentRepository.getInstance().getAppointmentsByDoctor(currentDoctor.getDoctorId(), new ApiCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                allAppointments.clear();
                if (result != null) {
                    allAppointments.addAll(result);
                }
                
                // Mặc định sắp xếp tất cả theo ngày hẹn (Sớm nhất lên trước)
                Collections.sort(allAppointments, new Comparator<Appointment>() {
                    @Override
                    public int compare(Appointment a1, Appointment a2) {
                        Date d1 = parseIsoToDate(a1.getAppointmentDateTime());
                        Date d2 = parseIsoToDate(a2.getAppointmentDateTime());
                        if (d1 == null || d2 == null) return 0;
                        return d1.compareTo(d2); // Tăng dần (Sớm nhất lên trước)
                    }
                });
                
                applyFilter();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AppointmentHistoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilter() {
        displayList.clear();
        if (selectedDateStr.isEmpty()) {
            displayList.addAll(allAppointments); // Đã được sort giảm dần ở loadWorkSchedule
        } else {
            for (Appointment appt : allAppointments) {
                String apptDate = formatToDateOnly(appt.getAppointmentDateTime()); // dd/MM/yyyy
                if (apptDate.equals(selectedDateStr)) {
                    displayList.add(appt);
                }
            }
            
            // Nếu đang xem theo ngày: Sắp xếp theo giờ (tăng dần 8h -> 17h) để bác sĩ dễ theo dõi
            Collections.sort(displayList, new Comparator<Appointment>() {
                @Override
                public int compare(Appointment a1, Appointment a2) {
                    Date d1 = parseIsoToDate(a1.getAppointmentDateTime());
                    Date d2 = parseIsoToDate(a2.getAppointmentDateTime());
                    if (d1 == null || d2 == null) return 0;
                    return d1.compareTo(d2); // Tăng dần
                }
            });
        }

        adapter.notifyDataSetChanged();
    }

    private Date parseIsoToDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return null;
        try {
            SimpleDateFormat isoFormat;
            if (isoDate.contains(".")) {
                isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            } else {
                isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            }
            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            return isoFormat.parse(isoDate);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatToDateOnly(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            // Cố gắng parse nhiều định dạng ISO khác nhau
            String cleanDate = isoDate;
            if (isoDate.contains(".")) {
                // Nếu có milliseconds, dùng format này
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(cleanDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return outFormat.format(date);
            } else {
                // Nếu không có milliseconds
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(cleanDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return outFormat.format(date);
            }
        } catch (Exception e) {
            // Fallback: cắt chuỗi nếu lỗi parse
            if (isoDate.length() >= 10) {
                String ymd = isoDate.substring(0, 10); // yyyy-MM-dd
                String[] parts = ymd.split("-");
                if (parts.length == 3) {
                    return parts[2] + "/" + parts[1] + "/" + parts[0];
                }
            }
            return "";
        }
    }

    @Override
    public void onCancel(Appointment appointment) {
        AppointmentRepository.getInstance().cancelAppointment(appointment.getAppointmentId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(AppointmentHistoryActivity.this, "Hủy lịch hẹn thành công", Toast.LENGTH_SHORT).show();
                loadWorkSchedule();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AppointmentHistoryActivity.this, "Lỗi khi hủy: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onComplete(Appointment appointment) {
        AppointmentRepository.getInstance().completeAppointment(appointment.getAppointmentId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(AppointmentHistoryActivity.this, "Hoàn thành lịch hẹn thành công", Toast.LENGTH_SHORT).show();
                loadWorkSchedule();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AppointmentHistoryActivity.this, "Lỗi khi hoàn thành: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}