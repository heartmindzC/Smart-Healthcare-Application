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
import com.example.smart_healthcare_application.models.TimeSlot;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<TimeSlot> timeSlotList;
    private int selectedPosition = -1;
    private OnTimeSlotSelectedListener listener;

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(TimeSlot timeSlot);
    }

    public TimeSlotAdapter(List<TimeSlot> timeSlotList, OnTimeSlotSelectedListener listener) {
        this.timeSlotList = timeSlotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot slot = timeSlotList.get(position);
        holder.bind(slot, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            if (slot.isBooked()) return;
            int prevSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (prevSelected != -1) notifyItemChanged(prevSelected);
            notifyItemChanged(selectedPosition);
            if (listener != null) listener.onTimeSlotSelected(slot);
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList != null ? timeSlotList.size() : 0;
    }

    public void resetSelection() {
        int prev = selectedPosition;
        selectedPosition = -1;
        if (prev != -1) notifyItemChanged(prev);
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        CardView cardTimeSlot;
        TextView tvTimeSlot;

        TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTimeSlot = itemView.findViewById(R.id.cardTimeSlot);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
        }

        void bind(TimeSlot slot, boolean isSelected) {
            tvTimeSlot.setText(slot.getDisplayTime());

            Context context = itemView.getContext();
            if (slot.isBooked()) {
                cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                tvTimeSlot.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                cardTimeSlot.setAlpha(0.6f);
            } else if (isSelected) {
                cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(R.color.teal_primary));
                tvTimeSlot.setTextColor(context.getResources().getColor(android.R.color.white));
                cardTimeSlot.setAlpha(1.0f);
            } else {
                cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                tvTimeSlot.setTextColor(context.getResources().getColor(R.color.teal_primary));
                cardTimeSlot.setAlpha(1.0f);
            }
        }
    }
}
