package com.example.alarm_hashai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String audioUriString = intent.getStringExtra("AUDIO_URI");

        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.setAction(AlarmService.ACTION_START);
        serviceIntent.putExtra("AUDIO_URI", audioUriString);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}