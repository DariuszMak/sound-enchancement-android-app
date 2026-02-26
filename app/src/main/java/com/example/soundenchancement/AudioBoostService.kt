package com.example.soundenchancement

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class AudioBoostService : Service() {

    companion object {
        var bassBoostFactory: (Int) -> IBassBoost = { RealBassBoost(it) }
        var loudnessFactory: (Int) -> ILoudnessEnhancer = { RealLoudnessEnhancer(it) }
    }

    private val binder = LocalBinder()

    private var audioTrack: AudioTrack? = null
    private var bassBoost: IBassBoost? = null
    private var loudnessEnhancer: ILoudnessEnhancer? = null

    inner class LocalBinder : android.os.Binder() {
        fun getService(): AudioBoostService = this@AudioBoostService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        initAudioSession()
        enableEffects()
        Log.d("AudioBoostService", "Service created")
    }

    private fun initAudioSession() {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(sampleRate)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),
            bufferSize,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
    }

    private fun enableEffects() {
        try {
            val sessionId = audioTrack?.audioSessionId ?: return

            bassBoost = bassBoostFactory(sessionId)
            loudnessEnhancer = loudnessFactory(sessionId)

            bassBoost?.enabled = true
            loudnessEnhancer?.enabled = true

        } catch (e: Exception) {
            Log.e("AudioBoostService", "Audio effects not supported", e)
        }
    }

    override fun onDestroy() {
        bassBoost?.release()
        loudnessEnhancer?.release()
        audioTrack?.release()

        bassBoost = null
        loudnessEnhancer = null
        audioTrack = null

        super.onDestroy()
    }

    fun getBassBoost(): IBassBoost? = bassBoost
    fun getLoudnessEnhancer(): ILoudnessEnhancer? = loudnessEnhancer

    private fun startForegroundService() {
        val channelId = "boost_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sound Booster",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sound Enhancement Active")
            .setContentText("Bass & Volume Booster Running")
            .setSmallIcon(R.mipmap.ic_launcher)
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