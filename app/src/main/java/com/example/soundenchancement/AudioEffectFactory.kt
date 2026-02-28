package com.example.soundenchancement

import android.media.audiofx.BassBoost

import android.media.MediaPlayer

class RealBassBoost(mediaPlayer: MediaPlayer) : IBassBoost {

    private val bass = BassBoost(0, mediaPlayer.audioSessionId)

    override var strength: Short
        get() = bass.roundedStrength
        set(value) {
            bass.setStrength(value)
        }

    override var enabled: Boolean
        get() = bass.enabled
        set(value) {
            bass.enabled = value
        }

    override fun release() {
        bass.release()
    }
}