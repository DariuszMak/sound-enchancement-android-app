package com.example.soundenchancement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var isBassActive = false

    // Maps each band SeekBar ID to its display value (0–100 scale matching the XML presets)
    private val bandIds = listOf(
        R.id.band60, R.id.band120, R.id.band250, R.id.band500,
        R.id.band2k, R.id.band4k, R.id.band8k, R.id.band16k
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.btnStart)
        val stopButton  = findViewById<Button>(R.id.btnStop)
        val statusDot   = findViewById<TextView>(R.id.statusDot)
        val statusLabel = findViewById<TextView>(R.id.statusLabel)

        // Disable all sliders — display only, not interactive
        bandIds.forEach { id ->
            findViewById<SeekBar>(id).isEnabled = false
        }

        fun updateStatus() {
            if (isBassActive) {
                statusDot.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
                statusLabel.text = "ON"
                statusLabel.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            } else {
                statusDot.setTextColor(0xFF444455.toInt())
                statusLabel.text = "OFF"
                statusLabel.setTextColor(0xFF888888.toInt())
            }
        }

        updateStatus()

        startButton.setOnClickListener {
            startService(Intent(this, AudioEffectService::class.java))
            isBassActive = true
            updateStatus()
        }

        stopButton.setOnClickListener {
            stopService(Intent(this, AudioEffectService::class.java))
            isBassActive = false
            updateStatus()
        }
    }
}