package com.example.smart_healthcare_application.factory;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.models.Appointment;

public class CompleteAppointmentUI extends AppointmentUI {
    private TextView tvDate, tvStatus, tvDoctor, tvHospital, tvReason;
    private Button btnViewPrescription;

    public CompleteAppointmentUI(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_complete, parent, false));

        tvDate = itemView.findViewById(R.id.tvDate);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvDoctor = itemView.findViewById(R.id.tvDoctor);
        tvHospital = itemView.findViewById(R.id.tvHospital);
        tvReason = itemView.findViewById(R.id.tvReason);
        btnViewPrescription = itemView.findViewById(R.id.btnViewPrescription);
    }

    @Override
    public void bind(Appointment appointment) {
        tvDate.setText(formatDateTime(appointment.getAppointmentDateTime()));
        tvStatus.setText(appointment.getStatus());
        tvDoctor.setText("BS. " + appointment.getDoctorName() + " - " + appointment.getDepartmentName());
        tvHospital.setText(appointment.getHospitalName());

        String reason = appointment.getReason();
        tvReason.setText("Lý do: " + (reason != null && !reason.isEmpty() ? reason : "Không có"));

        // Bắt sự kiện click nút Xem kết quả
        btnViewPrescription.setOnClickListener(v -> {
            // TODO: Chuyển màn hình xem đơn thuốc / kết quả khám
        });
    }
}