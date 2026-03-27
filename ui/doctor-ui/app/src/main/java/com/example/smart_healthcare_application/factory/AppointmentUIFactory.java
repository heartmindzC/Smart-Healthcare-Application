package com.example.smart_healthcare_application.factory;

import android.view.ViewGroup;


public class AppointmentUIFactory {
    public static final int TYPE_CONFIRM = 1;
    public static final int TYPE_COMPLETE = 2;
    public static final int TYPE_CANCEL = 3;

    public static AppointmentUI createViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CONFIRM:
                return new ConfirmAppointmentUI(parent);

            case TYPE_COMPLETE:
                return new CompleteAppointmentUI(parent);

            case TYPE_CANCEL:
                return new CancelAppointmentUI(parent);

            default:
                return new ConfirmAppointmentUI(parent);
        }
    }
}