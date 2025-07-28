package com.example.alarm_hashai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        Button btnDismiss = findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(v -> {
        Intent stopServiceIntent = new Intent(this, AlarmService.class);
        stopServiceIntent.setAction(AlarmService.ACTION_STOP);
        startService(stopServiceIntent);

        finish();
    });
    }
}