package com.example.alarm_hashai

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val audioUriString = intent.getStringExtra("AUDIO_URI")
                val audioUri = Uri.parse(audioUriString)
                startAlarm(audioUri)
            }
            ACTION_STOP -> {
                stopAlarm()
            }
        }
        return START_STICKY
    }


    private fun startAlarm(audioUri: Uri) {


        val activityIntent = Intent(this, AlarmScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(activityIntent)

        mediaPlayer = MediaPlayer.create(this, audioUri).apply {
            isLooping = true
            start()
        }
    }

    private fun stopAlarm() {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    }
