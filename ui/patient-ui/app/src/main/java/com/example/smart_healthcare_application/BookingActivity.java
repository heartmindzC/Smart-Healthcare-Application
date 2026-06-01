package com.example.smart_healthcare_application;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.DoctorAdapter;
import com.example.smart_healthcare_application.adapters.TimeSlotAdapter;
import com.example.smart_healthcare_application.adapters.TimeSlotDataAdapter;
import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.repository.AppointmentRepository;
import com.example.smart_healthcare_application.api.repository.DepartmentRepository;
import com.example.smart_healthcare_application.api.repository.DoctorRepository;
import com.example.smart_healthcare_application.api.repository.HospitalRepository;
import com.example.smart_healthcare_application.api.repository.TimeSlotRepository;
import com.example.smart_healthcare_application.api.response.TimeSlotResponse;
import com.example.smart_healthcare_application.models.Appointment;
import com.example.smart_healthcare_application.models.Department;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.models.Hospital;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.models.TimeSlot;
import com.example.smart_healthcare_application.utils.LoginManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    // --- Views ---
    private Spinner spinnerHospital, spinnerDepartment;
    private RecyclerView rvDoctors, rvTimeSlots;
    private LinearLayout layoutDoctorSection, layoutTimeslotSection, layoutDateSection, layoutConfirmSection;
    private TextView tvSelectedDate, tvSelectedDoctorName, tvSelectedDoctorSpec,
            tvSelectedTimeslot, tvSelectedDateConfirm;
    private android.widget.EditText etReason, etNotes;
    private Button btnPickDate, btnConfirmBooking;
    private ProgressBar progressBarDepartment, progressBarDoctor, progressBarTimeslot;
    private LinearLayout layoutAdditionalInfoSection;
    private View dividerDoctors, dividerTimeslot, dividerDate, dividerAdditionalInfo, dividerConfirm;

    // --- Data ---
    private List<Hospital> hospitalList = new ArrayList<>();
    private List<Department> departmentList = new ArrayList<>();
    private List<Doctor> doctorList = new ArrayList<>();
    private List<TimeSlot> timeSlotList = new ArrayList<>();

    private Hospital selectedHospital = null;
    private Department selectedDepartment = null;
    private Doctor selectedDoctor = null;
    private TimeSlot selectedTimeSlot = null;
    private String selectedDateStr = null; // "yyyy-MM-dd"

    // --- Adapters ---
    private ArrayAdapter<Hospital> hospitalSpinnerAdapter;
    private ArrayAdapter<Department> departmentSpinnerAdapter;
    private DoctorAdapter doctorAdapter;
    private TimeSlotAdapter timeSlotAdapter;

    // --- Repositories ---
    private HospitalRepository hospitalRepository;
    private DepartmentRepository departmentRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private TimeSlotRepository timeSlotRepository;

    // Flags to avoid re-triggering spinner listeners
    private boolean isHospitalSpinnerReady = false;
    private boolean isDepartmentSpinnerReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        hospitalRepository = new HospitalRepository();
        departmentRepository = new DepartmentRepository();
        doctorRepository = new DoctorRepository();
        appointmentRepository = new AppointmentRepository();
        timeSlotRepository = new TimeSlotRepository();

        initViews();
        buildTimeSlots();
        loadHospitals();
        setupListeners();
    }

    private void initViews() {
        spinnerHospital = findViewById(R.id.spinnerHospital);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        rvDoctors = findViewById(R.id.rvDoctors);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        layoutDoctorSection = findViewById(R.id.layoutDoctorSection);
        layoutTimeslotSection = findViewById(R.id.layoutTimeslotSection);
        layoutDateSection = findViewById(R.id.layoutDateSection);
        layoutAdditionalInfoSection = findViewById(R.id.layoutAdditionalInfoSection);
        layoutConfirmSection = findViewById(R.id.layoutConfirmSection);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedDoctorName = findViewById(R.id.tvSelectedDoctorName);
        tvSelectedDoctorSpec = findViewById(R.id.tvSelectedDoctorSpec);
        tvSelectedTimeslot = findViewById(R.id.tvSelectedTimeslot);
        tvSelectedDateConfirm = findViewById(R.id.tvSelectedDateConfirm);
        etReason = findViewById(R.id.etReason);
        etNotes = findViewById(R.id.etNotes);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        progressBarDepartment = findViewById(R.id.progressBarDepartment);
        progressBarDoctor = findViewById(R.id.progressBarDoctor);
        progressBarTimeslot = findViewById(R.id.progressBarTimeslot);
        dividerDoctors = findViewById(R.id.dividerDoctors);
        dividerTimeslot = findViewById(R.id.dividerTimeslot);
        dividerDate = findViewById(R.id.dividerDate);
        dividerAdditionalInfo = findViewById(R.id.dividerAdditionalInfo);
        dividerConfirm = findViewById(R.id.dividerConfirm);

        // Setup Hospital Spinner
        hospitalSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hospitalList);
        hospitalSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospital.setAdapter(hospitalSpinnerAdapter);

        // Setup Department Spinner
        departmentSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentList);
        departmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(departmentSpinnerAdapter);

        // Setup Doctor RecyclerView
        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(doctorList, doctor -> {
            selectedDoctor = doctor;
            selectedTimeSlot = null;
            selectedDateStr = null;
            tvSelectedDate.setText("Chưa chọn ngày");
            timeSlotAdapter.resetSelection();
            showDateSection();
            updateConfirmSection();
        });
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(doctorAdapter);

        // Setup TimeSlot RecyclerView
        timeSlotAdapter = new TimeSlotAdapter(timeSlotList, slot -> {
            selectedTimeSlot = slot;
            showAdditionalInfoSection();
            updateConfirmSection();
        });
        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 2));
        rvTimeSlots.setAdapter(timeSlotAdapter);

        // Initially hide sections
        layoutDoctorSection.setVisibility(View.GONE);
        dividerDoctors.setVisibility(View.GONE);
        layoutTimeslotSection.setVisibility(View.GONE);
        dividerTimeslot.setVisibility(View.GONE);
        layoutDateSection.setVisibility(View.GONE);
        dividerDate.setVisibility(View.GONE);
        layoutAdditionalInfoSection.setVisibility(View.GONE);
        dividerAdditionalInfo.setVisibility(View.GONE);
        layoutConfirmSection.setVisibility(View.GONE);
        dividerConfirm.setVisibility(View.GONE);
    }

    /**
     * Tạo các khung giờ cố định:
     * Bắt đầu 8:00, mỗi slot 1h30p, nghỉ 30p
     * 08:00–09:30, 10:00–11:30, 13:00–14:30, 15:00–16:30
     * (thêm break buổi trưa 12:00–13:00)
     */
    private void buildTimeSlots() {
        timeSlotList.clear();

        // Buổi sáng
        timeSlotList.add(new TimeSlot("slot_1", "08:00", "09:30", "08:00 - 09:30"));
        timeSlotList.add(new TimeSlot("slot_2", "10:00", "11:30", "10:00 - 11:30"));
        // Buổi chiều
        timeSlotList.add(new TimeSlot("slot_3", "13:00", "14:30", "13:00 - 14:30"));
        timeSlotList.add(new TimeSlot("slot_4", "15:00", "16:30", "15:00 - 16:30"));
    }

    private void setupListeners() {
        // Hospital Spinner
        spinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isHospitalSpinnerReady) return;
                Hospital hospital = hospitalList.get(position);
                if (hospital.equals(selectedHospital)) return;
                selectedHospital = hospital;
                resetFromDepartment();
                loadDepartments(hospital.getHospitalId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Department Spinner
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isDepartmentSpinnerReady) return;
                Department department = departmentList.get(position);
                if (department.equals(selectedDepartment)) return;
                selectedDepartment = department;
                resetFromDoctor();
                loadDoctors(department.getDepartmentId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Date picker button
        btnPickDate.setOnClickListener(v -> showDatePicker());

        // Confirm booking button
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void loadHospitals() {
        hospitalRepository.getAllHospitals(new ApiCallback<List<Hospital>>() {
            @Override
            public void onSuccess(List<Hospital> result) {
                runOnUiThread(() -> {
                    isHospitalSpinnerReady = false;
                    hospitalList.clear();
                    if (result != null) hospitalList.addAll(result);
                    hospitalSpinnerAdapter.notifyDataSetChanged();
                    isHospitalSpinnerReady = true;
                    if (!hospitalList.isEmpty()) {
                        spinnerHospital.setSelection(0);
                        selectedHospital = hospitalList.get(0);
                        loadDepartments(selectedHospital.getHospitalId());
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(BookingActivity.this,
                        "Không tải được danh sách bệnh viện: " + errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadDepartments(String hospitalId) {
        progressBarDepartment.setVisibility(View.VISIBLE);
        isDepartmentSpinnerReady = false;
        departmentList.clear();
        departmentSpinnerAdapter.notifyDataSetChanged();

        departmentRepository.getDepartmentsByHospital(hospitalId, new ApiCallback<List<Department>>() {
            @Override
            public void onSuccess(List<Department> result) {
                runOnUiThread(() -> {
                    progressBarDepartment.setVisibility(View.GONE);
                    departmentList.clear();
                    if (result != null) departmentList.addAll(result);
                    departmentSpinnerAdapter.notifyDataSetChanged();
                    isDepartmentSpinnerReady = true;
                    if (!departmentList.isEmpty()) {
                        spinnerDepartment.setSelection(0);
                        selectedDepartment = departmentList.get(0);
                        loadDoctors(selectedDepartment.getDepartmentId());
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    progressBarDepartment.setVisibility(View.GONE);
                    Toast.makeText(BookingActivity.this,
                            "Không tải được danh sách khoa: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadDoctors(String departmentId) {
        progressBarDoctor.setVisibility(View.VISIBLE);
        layoutDoctorSection.setVisibility(View.GONE);
        dividerDoctors.setVisibility(View.GONE);

        doctorRepository.getDoctorsByDepartment(departmentId, new ApiCallback<List<Doctor>>() {
            @Override
            public void onSuccess(List<Doctor> result) {
                runOnUiThread(() -> {
                    progressBarDoctor.setVisibility(View.GONE);
                    if (result != null && !result.isEmpty()) {
                        doctorAdapter.updateData(result);
                        layoutDoctorSection.setVisibility(View.VISIBLE);
                        dividerDoctors.setVisibility(View.VISIBLE);
                    } else {
                        layoutDoctorSection.setVisibility(View.GONE);
                        Toast.makeText(BookingActivity.this,
                                "Không có bác sĩ trong khoa này.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    progressBarDoctor.setVisibility(View.GONE);
                    Toast.makeText(BookingActivity.this,
                            "Không tải được danh sách bác sĩ: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showTimeslotSection() {
        layoutDateSection.setVisibility(View.VISIBLE);
        dividerDate.setVisibility(View.VISIBLE);
        layoutTimeslotSection.setVisibility(View.VISIBLE);
        dividerTimeslot.setVisibility(View.VISIBLE);
        
        layoutConfirmSection.setVisibility(View.GONE);
        dividerConfirm.setVisibility(View.GONE);
        layoutAdditionalInfoSection.setVisibility(View.GONE);
        dividerAdditionalInfo.setVisibility(View.GONE);
    }

    private void showDateSection() {
        layoutDateSection.setVisibility(View.VISIBLE);
        dividerDate.setVisibility(View.VISIBLE);
        layoutTimeslotSection.setVisibility(View.GONE);
        dividerTimeslot.setVisibility(View.GONE);
        layoutConfirmSection.setVisibility(View.GONE);
        dividerConfirm.setVisibility(View.GONE);
        layoutAdditionalInfoSection.setVisibility(View.GONE);
        dividerAdditionalInfo.setVisibility(View.GONE);
    }

    private void showAdditionalInfoSection() {
        layoutAdditionalInfoSection.setVisibility(View.VISIBLE);
        dividerAdditionalInfo.setVisibility(View.VISIBLE);
        layoutConfirmSection.setVisibility(View.VISIBLE);
        dividerConfirm.setVisibility(View.VISIBLE);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        // Chỉ cho phép chọn từ ngày mai trở đi
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (dp, y, m, d) -> {
            selectedDateStr = String.format("%04d-%02d-%02d", y, m + 1, d);
            String displayDate = String.format("%02d/%02d/%04d", d, m + 1, y);
            tvSelectedDate.setText(displayDate);
            loadBookedSlots();
            updateConfirmSection();
        }, year, month, day);

        // Không cho chọn ngày trong quá khứ
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private void loadBookedSlots() {
        if (selectedDoctor == null || selectedDateStr == null) return;
        
        progressBarTimeslot.setVisibility(View.VISIBLE);
        layoutTimeslotSection.setVisibility(View.VISIBLE);
        dividerTimeslot.setVisibility(View.VISIBLE);

        timeSlotRepository.getTimeSlotsByDoctorAndDate(selectedDoctor.getDoctorId(), selectedDateStr, new ApiCallback<List<TimeSlotResponse>>() {
            @Override
            public void onSuccess(List<TimeSlotResponse> result) {
                runOnUiThread(() -> {
                    progressBarTimeslot.setVisibility(View.GONE);
                    
                    // [Adapter Pattern] Chuyển đổi dữ liệu backend → frontend thông qua Adapter
                    TimeSlotDataAdapter.syncBookingStatus(timeSlotList, result);
                    
                    timeSlotAdapter.notifyDataSetChanged();
                    showTimeslotSection();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    progressBarTimeslot.setVisibility(View.GONE);
                    Log.e("BookingActivity", "Error loading doctor time slots: " + errorMessage);
                    Toast.makeText(BookingActivity.this, "Lỗi tải lịch làm việc: " + errorMessage, Toast.LENGTH_SHORT).show();
                    showTimeslotSection(); 
                });
            }
        });
    }

    private void updateConfirmSection() {
        if (selectedDoctor != null && selectedTimeSlot != null && selectedDateStr != null) {
            tvSelectedDoctorName.setText("BS. " + selectedDoctor.getFullName());
            tvSelectedDoctorSpec.setText(selectedDoctor.getDepartment() != null
                    ? selectedDoctor.getDepartment() : "Chuyên khoa chung");
            tvSelectedTimeslot.setText(selectedTimeSlot.getDisplayTime());

            // Format date for display
            String[] parts = selectedDateStr.split("-");
            String displayDate = parts[2] + "/" + parts[1] + "/" + parts[0];
            tvSelectedDateConfirm.setText(displayDate);
            
            layoutAdditionalInfoSection.setVisibility(View.VISIBLE);
            dividerAdditionalInfo.setVisibility(View.VISIBLE);
            layoutConfirmSection.setVisibility(View.VISIBLE);
            dividerConfirm.setVisibility(View.VISIBLE);
        }
    }

    private void confirmBooking() {
        if (selectedHospital == null || selectedDepartment == null
                || selectedDoctor == null || selectedTimeSlot == null || selectedDateStr == null) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient currentPatient = LoginManager.getInstance(this).getPatient();
        if (currentPatient == null) {
            Toast.makeText(this, "Không tìm thấy hồ sơ bệnh nhân. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo chuỗi appointmentDateTime: "yyyy-MM-ddTHH:mm:ss"
        String appointmentDateTime = selectedDateStr + "T" + selectedTimeSlot.getStartTime() + ":00";

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đặt lịch")
                .setMessage("Bạn có muốn đặt lịch khám?\n\n"
                        + "🏥 Bệnh viện: " + selectedHospital.getHospitalName() + "\n"
                        + "🏬 Khoa: " + selectedDepartment.getDepartmentName() + "\n"
                        + "👨‍⚕️ Bác sĩ: BS. " + selectedDoctor.getFullName() + "\n"
                        + "🕐 Giờ: " + selectedTimeSlot.getDisplayTime() + "\n"
                        + "📅 Ngày: " + tvSelectedDateConfirm.getText() + "\n"
                        + (etReason.getText().toString().isEmpty() ? "" : "📝 Lý do: " + etReason.getText().toString() + "\n")
                        + (etNotes.getText().toString().isEmpty() ? "" : "🗒️ Ghi chú: " + etNotes.getText().toString()))
                .setPositiveButton("Đặt lịch", (dialog, which) -> {
                    createAppointment(currentPatient, appointmentDateTime);
                })
                .setNegativeButton("Hủy", (d, w) -> d.dismiss())
                .show();
    }

    private void createAppointment(Patient patient, String appointmentDateTime) {
        btnConfirmBooking.setEnabled(false);

        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setPatientName(patient.getFullName());
        appointment.setDoctorId(selectedDoctor.getDoctorId());
        appointment.setDoctorName(selectedDoctor.getFullName());
        appointment.setHospitalId(selectedHospital.getHospitalId());
        appointment.setHospitalName(selectedHospital.getHospitalName());
        appointment.setDepartmentId(selectedDepartment.getDepartmentId());
        appointment.setDepartmentName(selectedDepartment.getDepartmentName());
        appointment.setTimeSlotId(selectedTimeSlot.getSlotId());
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setReason(etReason.getText().toString());
        appointment.setNotes(etNotes.getText().toString());
        appointment.setStatus("PENDING");

        appointmentRepository.createAppointment(appointment, new ApiCallback<Appointment>() {
            @Override
            public void onSuccess(Appointment result) {
                runOnUiThread(() -> {
                    btnConfirmBooking.setEnabled(true);
                    new AlertDialog.Builder(BookingActivity.this)
                            .setTitle("Đặt lịch thành công! 🎉")
                            .setMessage("Lịch khám của bạn đã được đặt thành công.\n\n"
                                    + "Vui lòng đến đúng giờ và mang theo CMND/CCCD.")
                            .setPositiveButton("OK", (d, w) -> {
                                d.dismiss();
                                finish();
                            })
                            .show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    btnConfirmBooking.setEnabled(true);
                    Toast.makeText(BookingActivity.this,
                            "Đặt lịch thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    // --- Reset helpers ---
    private void resetFromDepartment() {
        selectedDepartment = null;
        resetFromDoctor();
        departmentList.clear();
        departmentSpinnerAdapter.notifyDataSetChanged();
    }

    private void resetFromDoctor() {
        selectedDoctor = null;
        selectedTimeSlot = null;
        selectedDateStr = null;
        doctorAdapter.updateData(new ArrayList<>());
        timeSlotAdapter.resetSelection();
        layoutDoctorSection.setVisibility(View.GONE);
        dividerDoctors.setVisibility(View.GONE);
        layoutTimeslotSection.setVisibility(View.GONE);
        dividerTimeslot.setVisibility(View.GONE);
        layoutDateSection.setVisibility(View.GONE);
        dividerDate.setVisibility(View.GONE);
        layoutAdditionalInfoSection.setVisibility(View.GONE);
        dividerAdditionalInfo.setVisibility(View.GONE);
        layoutConfirmSection.setVisibility(View.GONE);
        dividerConfirm.setVisibility(View.GONE);
    }
}
