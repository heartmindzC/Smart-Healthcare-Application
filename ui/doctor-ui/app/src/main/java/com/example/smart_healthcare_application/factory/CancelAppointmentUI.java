package com.example.smart_healthcare_application.factory;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.models.Appointment;


public class CancelAppointmentUI extends AppointmentUI {
    private TextView tvDate, tvStatus, tvDoctor, tvHospital, tvCancelReason;

    public CancelAppointmentUI(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_cancel, parent, false));

        tvDate = itemView.findViewById(R.id.tvDate);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvDoctor = itemView.findViewById(R.id.tvDoctor);
        tvHospital = itemView.findViewById(R.id.tvHospital);
        tvCancelReason = itemView.findViewById(R.id.tvCancelReason);
    }

    @Override
    public void bind(Appointment appointment) {
        tvDate.setText(formatDateTime(appointment.getAppointmentDateTime()));
        tvStatus.setText(appointment.getStatus());
        tvDoctor.setText("BS. " + appointment.getDoctorName() + " - " + appointment.getDepartmentName());
        tvHospital.setText(appointment.getHospitalName());

        String notes = appointment.getNotes();
        tvCancelReason.setText("Lý do hủy: " + (notes != null && !notes.isEmpty() ? notes : "Bệnh nhân/Bác sĩ bận đột xuất"));
    }
}