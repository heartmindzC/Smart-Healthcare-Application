package com.example.smart_healthcare_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.models.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private int selectedPosition = -1;
    private OnDoctorSelectedListener listener;

    public interface OnDoctorSelectedListener {
        void onDoctorSelected(Doctor doctor);
    }

    public DoctorAdapter(List<Doctor> doctorList, OnDoctorSelectedListener listener) {
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.bind(doctor, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int prevSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (prevSelected != -1) notifyItemChanged(prevSelected);
            notifyItemChanged(selectedPosition);
            if (listener != null) listener.onDoctorSelected(doctor);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList != null ? doctorList.size() : 0;
    }

    public void updateData(List<Doctor> newList) {
        this.doctorList.clear();
        if (newList != null) this.doctorList.addAll(newList);
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public void resetSelection() {
        int prev = selectedPosition;
        selectedPosition = -1;
        if (prev != -1) notifyItemChanged(prev);
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        CardView cardDoctor;
        TextView tvDoctorName, tvDoctorSpecialization;

        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDoctor = itemView.findViewById(R.id.cardDoctor);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialization = itemView.findViewById(R.id.tvDoctorSpecialization);
        }

        void bind(Doctor doctor, boolean isSelected) {
            tvDoctorName.setText(doctor.getFullName() != null ? doctor.getFullName() : "N/A");
            tvDoctorSpecialization.setText(doctor.getDepartment() != null ? doctor.getDepartment() : "Chuyên khoa chung");

            Context context = itemView.getContext();
            if (isSelected) {
                cardDoctor.setCardBackgroundColor(context.getResources().getColor(R.color.doctor_selected_bg));
                cardDoctor.setCardElevation(8f);
            } else {
                cardDoctor.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                cardDoctor.setCardElevation(4f);
            }
        }
    }
}
