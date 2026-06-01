package com.example.smart_healthcare_application.adapters;

import com.example.smart_healthcare_application.api.response.TimeSlotResponse;
import com.example.smart_healthcare_application.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotDataAdapter {
    public static List<TimeSlotDisplayable> adaptFromBackend(List<TimeSlotResponse> backendSlots) {
        List<TimeSlotDisplayable> result = new ArrayList<>();
        if (backendSlots == null) return result;

        for (TimeSlotResponse response : backendSlots) {
            result.add(new TimeSlotResponseAdapter(response));
        }
        return result;
    }

    public static void syncBookingStatus(List<TimeSlot> localSlots, List<TimeSlotResponse> backendSlots) {
        for (TimeSlot slot : localSlots) {
            slot.setBooked(false);
        }

        if (backendSlots == null || backendSlots.isEmpty()) return;

        for (TimeSlotResponse backendSlot : backendSlots) {
            TimeSlotResponseAdapter adapter = new TimeSlotResponseAdapter(backendSlot);

            if (adapter.isBooked()) { 
                String adaptedStartTime = adapter.getStartTime(); 

                for (TimeSlot localSlot : localSlots) {
                    if (localSlot.getStartTime().equals(adaptedStartTime)) {
                        localSlot.setBooked(true);
                        break;
                    }
                }
            }
        }
    }
}
