package com.example.smart_healthcare_application.models;

public class TimeSlot {
    private String slotId;
    private String startTime;  // e.g. "08:00"
    private String endTime;    // e.g. "09:30"
    private String displayTime; // e.g. "08:00 - 09:30"
    private boolean isSelected;

    public TimeSlot(String slotId, String startTime, String endTime, String displayTime) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.displayTime = displayTime;
        this.isSelected = false;
    }

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getDisplayTime() { return displayTime; }
    public void setDisplayTime(String displayTime) { this.displayTime = displayTime; }

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
