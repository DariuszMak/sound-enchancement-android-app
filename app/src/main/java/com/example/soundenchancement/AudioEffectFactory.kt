package com.example.soundenchancement

import android.media.audiofx.BassBoost
import android.media.audiofx.LoudnessEnhancer

class RealBassBoost : IBassBoost {
    private val bass = BassBoost(0, 0)
    override val roundedStrength: Int
        get() = bass.roundedStrength.toInt()
    override var enabled: Boolean
        get() = bass.enabled
        set(value) { bass.enabled = value }
}

class RealLoudnessEnhancer(sessionId: Int) : ILoudnessEnhancer {
    private val loudness = LoudnessEnhancer(sessionId)
    override var targetGain: Int
        get() = loudness.targetGain.toInt()
        set(value) { loudness.setTargetGain(value) }
    override var enabled: Boolean
        get() = loudness.enabled
        set(value) { loudness.enabled = value }
}