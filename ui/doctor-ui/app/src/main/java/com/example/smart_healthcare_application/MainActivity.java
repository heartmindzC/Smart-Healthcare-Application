package com.example.smart_healthcare_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.LoginManager;

public class MainActivity extends AppCompatActivity {

    private CardView cvProfile, cvHistory;
    TextView tvWelcomeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ View
        initViews();

        User user = LoginManager.getInstance(MainActivity.this).getUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginManager.class);
            startActivity(intent);
            finish();
        }
        tvWelcomeTitle.setText("Xin chào, " + user.getFullname());

        // 1. Chức năng Xem thông tin cá nhân
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }

    public void initViews() {
        cvProfile = findViewById(R.id.cvProfile);
        cvHistory = findViewById(R.id.cvHistory);
        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
    }
}