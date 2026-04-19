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
        Date date = parseISODate(isoDate);
        if (date == null) return isoDate;
        try {
            SimpleDateFormat vnFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
            return vnFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }

    protected Date parseISODate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return null;

        // Thử các định dạng ISO 8601 phổ biến nhất
        String[] formats = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss"
        };

        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                if (isoDate.endsWith("Z") || isoDate.endsWith("z")) {
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                }
                Date date = sdf.parse(isoDate);
                if (date != null) return date;
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}