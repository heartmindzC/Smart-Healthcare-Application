package com.example.smart_healthcare_application.adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.factory.AppointmentUI;
import com.example.smart_healthcare_application.factory.AppointmentUIFactory;
import com.example.smart_healthcare_application.factory.ConfirmAppointmentUI;
import com.example.smart_healthcare_application.models.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentUI> {

    private List<Appointment> appointmentList;
    private OnCancelAppointmentListener cancelListener;

    public AppointmentAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public void setCancelListener(OnCancelAppointmentListener listener) {
        this.cancelListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        String status = appointmentList.get(position).getStatus();

        if (status == null) return AppointmentUIFactory.TYPE_CONFIRM;

        if (status.equalsIgnoreCase("PENDING") || status.equalsIgnoreCase("CONFIRMED")) {
            return AppointmentUIFactory.TYPE_CONFIRM;
        } else if (status.equalsIgnoreCase("COMPLETED") || status.equalsIgnoreCase("DONE")) {
            return AppointmentUIFactory.TYPE_COMPLETE;
        } else if (status.equalsIgnoreCase("CANCELLED")) {
            return AppointmentUIFactory.TYPE_CANCEL;
        }

        return AppointmentUIFactory.TYPE_CONFIRM;
    }

    @NonNull
    @Override
    public AppointmentUI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return AppointmentUIFactory.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentUI holder, int position) {
        Appointment appointment = appointmentList.get(position);
        if (holder instanceof ConfirmAppointmentUI) {
            ((ConfirmAppointmentUI) holder).setCancelListener(cancelListener);
        }
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointmentList != null ? appointmentList.size() : 0;
    }

    public void updateData(List<Appointment> newList) {
        this.appointmentList.clear();
        if (newList != null) {
            this.appointmentList.addAll(newList);
        }
        notifyDataSetChanged();
    }
}