package com.example.smart_healthcare_application;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
//    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Gọi API để lấy danh sách Appointment truyền vào đây
        // List<Appointment> myAppointments = fetchAppointmentsFromApi();
        // adapter = new AppointmentAdapter(myAppointments);
        // rvAppointments.setAdapter(adapter);
    }
}