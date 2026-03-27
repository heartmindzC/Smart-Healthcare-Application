package com.example.smart_healthcare_application.factory;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class AppointmentUI extends RecyclerView.ViewHolder {

    public AppointmentUI(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(Appointment appointment);

    protected String formatDateTime(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDate);

            SimpleDateFormat vnFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
            return vnFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
}