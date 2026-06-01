package com.example.smart_healthcare_application.adapters;

/**
 * [Adapter Pattern - TARGET INTERFACE]
 *
 * Đây là interface mà tầng UI (frontend) phụ thuộc vào.
 * Frontend không cần biết dữ liệu đến từ đâu (backend hay local),
 * chỉ cần tương tác thông qua interface này.
 */
public interface TimeSlotDisplayable {

    String getSlotId();

    String getStartTime();

    String getEndTime();

    String getDisplayTime();

    boolean isBooked();

    boolean isSelected();

    void setBooked(boolean booked);

    void setSelected(boolean selected);
}
