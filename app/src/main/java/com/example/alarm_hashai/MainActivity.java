// MainActivity.java
package com.example.alarm_hashai;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetAlarm;
    private Button btnSelectAudio;
    private Button btnViewAlarms;
    private TextView tvSelectedAudioFile;
    private Uri selectedAudioUri = null;
    private List<AlarmItem> alarmList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

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

        initViews();
        setupSharedPreferences();
        loadAlarms();
        setupClickListeners();
    }

    private void initViews() {
        timePicker = findViewById(R.id.timePicker);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnSelectAudio = findViewById(R.id.btnSelectAudio);
        btnViewAlarms = findViewById(R.id.btnViewAlarms);
        tvSelectedAudioFile = findViewById(R.id.tvSelectedAudioFile);
    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
    }

    private void setupClickListeners() {
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

        btnViewAlarms.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmsListActivity.class);
            startActivity(intent);
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

        int alarmId = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("AUDIO_URI", selectedAudioUri.toString());
        intent.putExtra("ALARM_ID", alarmId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        // Create alarm item and add to list
        String fileName = getFileNameFromUri(selectedAudioUri);
        AlarmItem alarmItem = new AlarmItem(
                alarmId,
                timePicker.getHour(),
                timePicker.getMinute(),
                selectedAudioUri.toString(),
                fileName,
                true,
                calendar.getTimeInMillis()
        );

        alarmList.add(alarmItem);
        saveAlarms();

        String timeString = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
        Toast.makeText(this, "Alarm set for " + timeString, Toast.LENGTH_SHORT).show();

        selectedAudioUri = null;
        tvSelectedAudioFile.setText("No file selected");
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = uri.getLastPathSegment();
        if (fileName != null && fileName.contains(":")) {
            String[] parts = fileName.split(":");
            fileName = parts[parts.length - 1];
        }
        return fileName != null ? fileName : "Unknown";
    }

    private void saveAlarms() {
        String json = gson.toJson(alarmList);
        sharedPreferences.edit().putString("alarm_list", json).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarms();
    }

    private void loadAlarms() {
        String json = sharedPreferences.getString("alarm_list", "");
        if (!json.isEmpty()) {
            Type listType = new TypeToken<List<AlarmItem>>() {}.getType();
            List<AlarmItem> savedAlarms = gson.fromJson(json, listType);
            if (savedAlarms != null) {
                alarmList.clear();
                alarmList.addAll(savedAlarms);
            }
        }
    }
}