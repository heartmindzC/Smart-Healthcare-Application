package com.example.smart_healthcare_application.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConfirmAppointmentUI extends AppointmentUI {
    private TextView tvDate, tvStatus, tvDoctor, tvHospital, tvReason;
    private Button btnCancelAppointment;

    public ConfirmAppointmentUI(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_confirm, parent, false));

        tvDate = itemView.findViewById(R.id.tvDate);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvDoctor = itemView.findViewById(R.id.tvDoctor);
        tvHospital = itemView.findViewById(R.id.tvHospital);
        tvReason = itemView.findViewById(R.id.tvReason);
        btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
    }

    @Override
    public void bind(Appointment appointment) {
        tvDate.setText(formatDateTime(appointment.getAppointmentDateTime()));
        tvStatus.setText(appointment.getStatus());
        tvDoctor.setText("BS. " + appointment.getDoctorName() + " - " + appointment.getDepartmentName());
        tvHospital.setText(appointment.getHospitalName());

        String reason = appointment.getReason();
        tvReason.setText("Lý do: " + (reason != null && !reason.isEmpty() ? reason : "Không có"));

        if (canCancelAppointment(appointment.getAppointmentDateTime())) {
            btnCancelAppointment.setVisibility(View.VISIBLE);
        } else {
            btnCancelAppointment.setVisibility(View.GONE);
        }

        btnCancelAppointment.setOnClickListener(v -> {
            // TODO: Gọi interface callback truyền ra Activity để xử lý gọi API hủy lịch
        });
    }

    // Hàm kiểm tra thời gian
    private boolean canCancelAppointment(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return false;

        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date appointmentDate = isoFormat.parse(isoDate);

            if (appointmentDate != null) {
                long currentTimeMillis = System.currentTimeMillis();
                long appointmentTimeMillis = appointmentDate.getTime();
                long diffInMillis = appointmentTimeMillis - currentTimeMillis;

                long hours24InMillis = 24 * 60 * 60 * 1000L;

                return diffInMillis > hours24InMillis;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}