package com.example.smart_healthcare_application.adapters;

import com.example.smart_healthcare_application.api.response.TimeSlotResponse;

public class TimeSlotResponseAdapter implements TimeSlotDisplayable {

    private final TimeSlotResponse backendSlot;
    private boolean isSelected = false;
    private boolean isBookedOverride = false;
    private boolean isBookedSet = false;

    public TimeSlotResponseAdapter(TimeSlotResponse backendSlot) {
        this.backendSlot = backendSlot;
    }

    @Override
    public String getSlotId() {
        return backendSlot.getTimeSlotId();
    }

    @Override
    public String getStartTime() {
        String raw = backendSlot.getStartTime();
        if (raw != null && raw.length() >= 5) {
            return raw.substring(0, 5);
        }
        return raw != null ? raw : "";
    }

    @Override
    public String getEndTime() {
        String raw = backendSlot.getEndTime();
        if (raw != null && raw.length() >= 5) {
            return raw.substring(0, 5);
        }
        return raw != null ? raw : "";
    }

    @Override
    public String getDisplayTime() {
        return getStartTime() + " - " + getEndTime();
    }

    @Override
    public boolean isBooked() {
        if (isBookedSet) return isBookedOverride;
        return !backendSlot.isAvailable();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setBooked(boolean booked) {
        this.isBookedOverride = booked;
        this.isBookedSet = true;
    }

    @Override
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }


    public TimeSlotResponse getBackendSlot() {
        return backendSlot;
    }
}
