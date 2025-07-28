package com.example.alarm_hashai;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                String audioUriString = intent.getStringExtra("AUDIO_URI");
                Uri audioUri = Uri.parse(audioUriString);
                startAlarm(audioUri);
            } else if (ACTION_STOP.equals(action)) {
                stopAlarm();
            }
        }
        return START_STICKY;
    }

    private void startAlarm(Uri audioUri) {
        Intent activityIntent = new Intent(this, AlarmScreenActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);

        mediaPlayer = MediaPlayer.create(this, audioUri);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}