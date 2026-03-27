package com.example.smart_healthcare_application.factory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.adapters.OnCancelAppointmentListener;
import com.example.smart_healthcare_application.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConfirmAppointmentUI extends AppointmentUI {
    private TextView tvDate, tvStatus, tvDoctor, tvHospital, tvReason;
    private Button btnCancelAppointment;

    private OnCancelAppointmentListener cancelListener;

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

    public void setCancelListener(OnCancelAppointmentListener listener) {
        this.cancelListener = listener;
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
            if (cancelListener != null) {
                cancelListener.onCancelAppointment(appointment.getAppointmentId());
            }
        });
    }

    // Hàm kiểm tra thời gian
    private boolean canCancelAppointment(String isoDate) {
        Date appointmentDate = parseISODate(isoDate);

        if (appointmentDate != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long appointmentTimeMillis = appointmentDate.getTime();
            long diffInMillis = appointmentTimeMillis - currentTimeMillis;

            long hours24InMillis = 24 * 60 * 60 * 1000L;

            return diffInMillis > hours24InMillis;
        }
        return false;
    }
}