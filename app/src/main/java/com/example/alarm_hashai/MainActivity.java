// MainActivity.java
package com.example.alarm_hashai;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetAlarm;
    private Button btnSelectAudio;
    private TextView tvSelectedAudioFile;
    private Uri selectedAudioUri = null;

    private ActivityResultLauncher<String[]> audioPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    selectedAudioUri = uri;
                    getApplicationContext().getContentResolver()
                            .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    String fileName = uri.getLastPathSegment();
                    if (fileName != null && fileName.contains(":")) {
                        String[] parts = fileName.split(":");
                        fileName = parts[parts.length - 1];
                    }
                    tvSelectedAudioFile.setText("Selected: " + fileName);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnSelectAudio = findViewById(R.id.btnSelectAudio);
        tvSelectedAudioFile = findViewById(R.id.tvSelectedAudioFile);

        btnSelectAudio.setOnClickListener(v -> {
            audioPickerLauncher.launch(new String[]{"audio/*"});
        });

        btnSetAlarm.setOnClickListener(v -> {
            if (selectedAudioUri != null) {
                setAlarm();
            } else {
                Toast.makeText(this, "Please select an alarm sound first", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
            Toast.makeText(this, "Please grant permission to schedule exact alarms", Toast.LENGTH_LONG).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("AUDIO_URI", selectedAudioUri.toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        Toast.makeText(this, "Alarm set for " + timePicker.getHour() + ":" + timePicker.getMinute(),
                Toast.LENGTH_SHORT).show();
    }
}