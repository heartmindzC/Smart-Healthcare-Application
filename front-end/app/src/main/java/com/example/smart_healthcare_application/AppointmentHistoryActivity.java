package com.example.smart_healthcare_application;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.adapters.AppointmentAdapter;
import com.example.smart_healthcare_application.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        initViews();
        loadMockData();
    }

    private void initViews() {
        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        rvAppointments.setAdapter(adapter);
    }

    private void loadMockData() {
        List<Appointment> mockList = new ArrayList<>();

        // 1. Cuộc hẹn 1: Trạng thái PENDING (Chờ khám) -> Sẽ render giao diện Confirm có nút "Hủy lịch"
        Appointment appt1 = new Appointment();
        appt1.setAppointmentId("APT-001");
        appt1.setDoctorName("Trần Trọng Khang");
        appt1.setDepartmentName("Khoa Tim Mạch");
        appt1.setHospitalName("Bệnh viện Đa khoa Quốc tế");
        appt1.setAppointmentDateTime("2026-03-28T08:30:00.000Z");
        appt1.setStatus("PENDING");
        appt1.setReason("Khám định kỳ nhịp tim");
        mockList.add(appt1);

        // 2. Cuộc hẹn 2: Trạng thái COMPLETED (Đã khám xong) -> Sẽ render giao diện Complete màu xanh có nút "Xem đơn thuốc"
        Appointment appt2 = new Appointment();
        appt2.setAppointmentId("APT-002");
        appt2.setDoctorName("Lê Thị Lan");
        appt2.setDepartmentName("Khoa Tiêu Hóa");
        appt2.setHospitalName("Phòng khám Đa khoa Tâm Anh");
        appt2.setAppointmentDateTime("2026-03-20T14:15:00.000Z");
        appt2.setStatus("COMPLETED");
        appt2.setReason("Đau dạ dày kéo dài");
        mockList.add(appt2);

        // 3. Cuộc hẹn 3: Trạng thái CANCELLED (Đã hủy) -> Sẽ render giao diện Cancel màu xám mờ không có nút
        Appointment appt3 = new Appointment();
        appt3.setAppointmentId("APT-003");
        appt3.setDoctorName("Nguyễn Văn Kiểm");
        appt3.setDepartmentName("Khoa Răng Hàm Mặt");
        appt3.setHospitalName("Nha khoa Nụ Cười");
        appt3.setAppointmentDateTime("2026-03-22T09:00:00.000Z");
        appt3.setStatus("CANCELLED");
        appt3.setReason("Nhổ răng khôn");
        appt3.setNotes("Bác sĩ bận đi công tác đột xuất");
        mockList.add(appt3);

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        long currentTime = System.currentTimeMillis();
        long oneHour = 60 * 60 * 1000L;

        // ---------------------------------------------------------
        // TEST CASE 1: Lịch hẹn cách hiện tại 48 tiếng (> 24h)
        // Kết quả mong đợi: HIỆN NÚT HỦY LỊCH
        // ---------------------------------------------------------
        Date date48HoursLater = new Date(currentTime + (48 * oneHour));

        Appointment appt5 = new Appointment();
        appt1.setAppointmentId("APT-TEST-01");
        appt1.setDoctorName("Trần Trọng Khang");
        appt1.setDepartmentName("Khoa Tim Mạch");
        appt1.setHospitalName("Bệnh viện Đa khoa Quốc tế");
        appt1.setAppointmentDateTime(isoFormat.format(date48HoursLater)); // Set giờ động
        appt1.setStatus("PENDING");
        appt1.setReason("Khám > 24h (Phải thấy nút hủy)");
        mockList.add(appt1);

        // ---------------------------------------------------------
        // TEST CASE 2: Lịch hẹn cách hiện tại 10 tiếng (< 24h)
        // Kết quả mong đợi: ẨN NÚT HỦY LỊCH
        // ---------------------------------------------------------
        Date date10HoursLater = new Date(currentTime + (10 * oneHour));

        Appointment appt6 = new Appointment();
        appt2.setAppointmentId("APT-TEST-02");
        appt2.setDoctorName("Lê Thị Lan");
        appt2.setDepartmentName("Khoa Tiêu Hóa");
        appt2.setHospitalName("Phòng khám Đa khoa Tâm Anh");
        appt2.setAppointmentDateTime(isoFormat.format(date10HoursLater)); // Set giờ động
        appt2.setStatus("PENDING");
        appt2.setReason("Khám < 24h (Nút hủy phải bị ẩn)");
        mockList.add(appt2);

        // Đổ dữ liệu vào Adapter và yêu cầu render lại giao diện
        adapter.updateData(mockList);
    }
}