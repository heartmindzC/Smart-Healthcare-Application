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
    private Button btnCancelAppointment, btnCompleteAppointment;
    private OnAppointmentActionListener listener;

    public interface OnAppointmentActionListener {
        void onCancel(Appointment appointment);
        void onComplete(Appointment appointment);
    }

    public void setActionListener(OnAppointmentActionListener listener) {
        this.listener = listener;
    }

    public ConfirmAppointmentUI(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_confirm, parent, false));

        tvDate = itemView.findViewById(R.id.tvDate);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvDoctor = itemView.findViewById(R.id.tvDoctor);
        tvHospital = itemView.findViewById(R.id.tvHospital);
        tvReason = itemView.findViewById(R.id.tvReason);
        btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
        btnCompleteAppointment = itemView.findViewById(R.id.btnCompleteAppointment);
    }

    @Override
    public void bind(Appointment appointment) {
        tvDate.setText(formatDateTime(appointment.getAppointmentDateTime()));
        tvStatus.setText(appointment.getStatus());
        tvDoctor.setText("BS. " + appointment.getDoctorName() + " - " + appointment.getDepartmentName());
        tvHospital.setText(appointment.getHospitalName());

        String reason = appointment.getReason();
        tvReason.setText("Lý do: " + (reason != null && !reason.isEmpty() ? reason : "Không có"));

        // Bác sĩ có quyền hủy hoặc hoàn thành bất cứ lúc nào
        btnCancelAppointment.setOnClickListener(v -> {
            if (listener != null) listener.onCancel(appointment);
        });

        btnCompleteAppointment.setOnClickListener(v -> {
            if (listener != null) listener.onComplete(appointment);
        });
    }
}