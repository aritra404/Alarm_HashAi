// AlarmsListActivity.java
package com.example.alarm_hashai;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmsListActivity extends AppCompatActivity implements AlarmAdapter.OnAlarmActionListener {

    private RecyclerView recyclerViewAlarms;
    private TextView tvNoAlarms;
    private AlarmAdapter alarmAdapter;
    private List<AlarmItem> alarmList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);

        initViews();
        setupToolbar();
        setupSharedPreferences();
        loadAlarms();
        setupRecyclerView();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewAlarms = findViewById(R.id.recyclerViewAlarms);
        tvNoAlarms = findViewById(R.id.tvNoAlarms);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Alarms");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
    }

    private void setupRecyclerView() {
        alarmAdapter = new AlarmAdapter(alarmList, this);
        recyclerViewAlarms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarms.setAdapter(alarmAdapter);
        updateNoAlarmsVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAlarms();
        alarmAdapter.notifyDataSetChanged();
        updateNoAlarmsVisibility();
    }

    @Override
    public void onCancelAlarm(AlarmItem alarmItem) {
        cancelAlarm(alarmItem);
        int position = alarmList.indexOf(alarmItem);
        alarmList.remove(alarmItem);
        saveAlarms();
        alarmAdapter.notifyItemRemoved(position);
        updateNoAlarmsVisibility();
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggleAlarm(AlarmItem alarmItem, boolean isEnabled) {
        alarmItem.setEnabled(isEnabled);
        if (isEnabled) {

            scheduleAlarm(alarmItem);
            Toast.makeText(this, "Alarm enabled", Toast.LENGTH_SHORT).show();
        } else {

            cancelAlarm(alarmItem);
            Toast.makeText(this, "Alarm disabled", Toast.LENGTH_SHORT).show();
        }
        saveAlarms();
        int position = alarmList.indexOf(alarmItem);
        alarmAdapter.notifyItemChanged(position);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAlarm(AlarmItem alarmItem) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmItem.getHour());
        calendar.set(Calendar.MINUTE, alarmItem.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("AUDIO_URI", alarmItem.getAudioUri());
        intent.putExtra("ALARM_ID", alarmItem.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmItem.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );

        alarmItem.setScheduledTime(calendar.getTimeInMillis());
    }

    private void cancelAlarm(AlarmItem alarmItem) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmItem.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private void saveAlarms() {
        String json = gson.toJson(alarmList);
        sharedPreferences.edit().putString("alarm_list", json).apply();
    }

    private void loadAlarms() {
        String json = sharedPreferences.getString("alarm_list", "");
        if (!json.isEmpty()) {
            try {
                JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
                alarmList.clear();

                for (JsonElement element : jsonArray) {
                    AlarmItem alarm = gson.fromJson(element, AlarmItem.class);
                    if (alarm != null) {
                        alarmList.add(alarm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                alarmList.clear();
            }
        }
    }


    private void updateNoAlarmsVisibility() {
        if (alarmList.isEmpty()) {
            tvNoAlarms.setVisibility(View.VISIBLE);
            recyclerViewAlarms.setVisibility(View.GONE);
        } else {
            tvNoAlarms.setVisibility(View.GONE);
            recyclerViewAlarms.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null) {
            String subtitle = alarmList.size() == 1 ?
                    "1 alarm" : alarmList.size() + " alarms";
            getSupportActionBar().setSubtitle(subtitle);
        }
    }
}