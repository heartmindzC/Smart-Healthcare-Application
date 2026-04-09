package com.example.smart_healthcare_application.models;

import com.example.smart_healthcare_application.adapters.TimeSlotDisplayable;

public class TimeSlot implements TimeSlotDisplayable {
    private String slotId;
    private String startTime;
    private String endTime;
    private String displayTime;
    private boolean isSelected;
    private boolean isBooked;

    public TimeSlot(String slotId, String startTime, String endTime, String displayTime) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.displayTime = displayTime;
        this.isSelected = false;
        this.isBooked = false;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
