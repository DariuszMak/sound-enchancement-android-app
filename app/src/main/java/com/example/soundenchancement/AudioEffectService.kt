package com.example.soundenchancement

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.audiofx.Equalizer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class AudioEffectService : Service() {

    private val binder = LocalBinder()
    private var equalizer: Equalizer? = null

    inner class LocalBinder : android.os.Binder() {
        fun getService(): AudioEffectService = this@AudioEffectService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        enableGlobalBassBoost()
        Log.d("AudioBoostService", "Global Bass Boost Enabled")
    }

    /**
     * Enable a bass boost by increasing the gain of low frequency bands.
     * Uses the Equalizer API on session 0 (global output).
     */
    private fun enableGlobalBassBoost(level: Int = 1000) {
        try {
            // Session 0 = global audio output
            equalizer = Equalizer(0, 0)
            equalizer?.enabled = true

            val numberOfBands = equalizer!!.numberOfBands
            for (i in 0 until numberOfBands) {
                val freq = equalizer!!.getCenterFreq(i.toShort())
                // Boost only low frequencies (< 250 Hz)
                if (freq <= 250_000) {
                    val maxLevel = equalizer!!.bandLevelRange[1]
                    val minLevel = equalizer!!.bandLevelRange[0]
                    val boost = (level / 1000.0 * (maxLevel - minLevel)).toInt() + minLevel
                    equalizer!!.setBandLevel(i.toShort(), boost.toShort())
                }
            }
            Log.d("AudioBoostService", "Equalizer bass bands boosted")
        } catch (e: Exception) {
            Log.e("AudioBoostService", "Equalizer not supported on this device/output", e)
        }
    }

    fun setBassBoostLevel(level: Int) {
        enableGlobalBassBoost(level.coerceIn(0, 1000))
    }

    override fun onDestroy() {
        try {
            equalizer?.release()
            equalizer = null
        } catch (e: Exception) {
            Log.e("AudioBoostService", "Error releasing Equalizer", e)
        }
        super.onDestroy()
    }

    private fun startForegroundService() {
        val channelId = "boost_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Bass Booster",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Bass Boost Active")
            .setContentText("Boosting system bass")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(1, notification)
        }
    }
}