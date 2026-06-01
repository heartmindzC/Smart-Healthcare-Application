package com.example.smart_healthcare_application.adapters; // Đổi package của bạn

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_healthcare_application.R;
import com.example.smart_healthcare_application.models.MedicalVisits;
import com.example.smart_healthcare_application.models.Prescription;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.VisitViewHolder> {

    private Context context;
    private List<MedicalVisits> historyList;

    public MedicalHistoryAdapter(Context context, List<MedicalVisits> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_history, parent, false);
        return new VisitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
        MedicalVisits visit = historyList.get(position);

        holder.tvVisitDate.setText("Ngày khám: " + formatDateString(visit.getVisitDate()));
        holder.tvHospitalDept.setText(visit.getHospital() + " - " + visit.getDepartment());
        holder.tvDoctor.setText("BS Điều trị: " + visit.getDoctorName());
        holder.tvDiagnosis.setText(visit.getDiagnosis());

        List<Prescription> prescriptions = visit.getPrescriptions();
        holder.llPrescriptionContainer.removeAllViews();

        if (prescriptions != null && !prescriptions.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(context);
            holder.llPrescriptionContainer.setVisibility(View.VISIBLE);

            for (Prescription med : prescriptions) {
                View medView = inflater.inflate(R.layout.item_prescription, holder.llPrescriptionContainer, false);
                TextView tvMedName = medView.findViewById(R.id.tvMedName);
                TextView tvQuantity = medView.findViewById(R.id.tvQuantity);
                TextView tvDosageFreq = medView.findViewById(R.id.tvDosageFreq);
                TextView tvInstructions = medView.findViewById(R.id.tvInstructions);
                tvMedName.setText(med.getMedicationName());
                tvQuantity.setText("SL: " + med.getQuantity());
                tvDosageFreq.setText(med.getDosage() + " - " + med.getFrequency());
                tvInstructions.setText("HD: " + med.getInstructions());

                holder.llPrescriptionContainer.addView(medView);
            }
        } else {
            holder.llPrescriptionContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    private String formatDateString(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
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
                if (date != null) {
                    SimpleDateFormat vnFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
                    return vnFormat.format(date);
                }
            } catch (Exception ignored) {
            }
        }
        return isoDate;
    }

    public static class VisitViewHolder extends RecyclerView.ViewHolder {
        TextView tvVisitDate, tvHospitalDept, tvDoctor, tvDiagnosis;
        LinearLayout llPrescriptionContainer;

        public VisitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVisitDate = itemView.findViewById(R.id.tvVisitDate);
            tvHospitalDept = itemView.findViewById(R.id.tvHospitalDept);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            llPrescriptionContainer = itemView.findViewById(R.id.llPrescriptionContainer);
        }
    }
}