package com.example.smart_healthcare_application.api.response;

import com.google.gson.annotations.SerializedName;

public class TimeSlotResponse {
    @SerializedName("timeSlotId")
    private String timeSlotId;

    @SerializedName("doctorId")
    private String doctorId;

    @SerializedName("startTime")
    private String startTime;  // e.g. "08:00:00"

    @SerializedName("endTime")
    private String endTime;    // e.g. "09:30:00"

    @SerializedName("isAvailable")
    private boolean isAvailable;

    @SerializedName("specificDate")
    private String specificDate;

    // Getters and Setters
    public String getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(String timeSlotId) { this.timeSlotId = timeSlotId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getSpecificDate() { return specificDate; }
    public void setSpecificDate(String specificDate) { this.specificDate = specificDate; }
}
