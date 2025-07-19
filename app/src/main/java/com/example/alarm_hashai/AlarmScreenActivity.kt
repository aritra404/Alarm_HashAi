package com.example.alarm_hashai

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_screen)


        val btnDismiss: Button = findViewById(R.id.btnDismiss)
        btnDismiss.setOnClickListener {

            val stopServiceIntent = Intent(this, AlarmService::class.java).apply {
                action = AlarmService.ACTION_STOP
            }
            startService(stopServiceIntent)

            finish()
        }
    }
}