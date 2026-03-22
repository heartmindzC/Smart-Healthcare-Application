package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cvProfile, cvHistory, cvBooking, cvEHR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        cvProfile = findViewById(R.id.cvProfile);
        cvHistory = findViewById(R.id.cvHistory);
        cvBooking = findViewById(R.id.cvBooking);
        cvEHR = findViewById(R.id.cvEHR);

        // 1. Chức năng Xem thông tin cá nhân
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Mở trang Thông tin cá nhân", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                 startActivity(intent);
            }
        });

        // 2. Chức năng Xem lịch sử
        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Mở trang Lịch sử khám", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity.this, AppointmentHistoryActivity.class);
                 startActivity(intent);
            }
        });

        // 3. Chức năng Đặt lịch khám
        cvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Mở trang Đặt lịch khám", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                // startActivity(intent);
            }
        });

        // 4. Chức năng Xem hồ sơ EHR
        cvEHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Mở trang Hồ sơ bệnh án (EHR)", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(MainActivity.this, EHRActivity.class);
                 startActivity(intent);
            }
        });
    }
}