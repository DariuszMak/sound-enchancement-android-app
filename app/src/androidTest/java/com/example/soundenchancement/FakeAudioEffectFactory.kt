package com.example.soundenchancement

import android.media.audiofx.BassBoost
import android.media.audiofx.LoudnessEnhancer

class FakeBassBoost : BassBoost(0, 0) {
    private var _enabled = true
    override fun setStrength(strength: Short): Int { return 0 }
    override fun setEnabled(enabled: Boolean): Int {
        _enabled = enabled
        return 0
    }
    override fun getEnabled(): Boolean = _enabled
    override val roundedStrength: Short
        get() = 1000.toShort()
}

class FakeLoudnessEnhancer(sessionId: Int) : LoudnessEnhancer(sessionId) {
    private var _enabled = true
    private var _gain = 1000
    override fun setTargetGain(gain: Int) { _gain = gain }
    override fun getTargetGain(): Int = _gain
    override fun setEnabled(enabled: Boolean) { _enabled = enabled }
    override fun getEnabled(): Boolean = _enabled
}

class FakeAudioEffectFactory : AudioEffectFactory() {
    override fun createLoudnessEnhancer(sessionId: Int): LoudnessEnhancer {
        return FakeLoudnessEnhancer(sessionId)
    }

    override fun createBassBoost(sessionId: Int): BassBoost {
        return FakeBassBoost()
    }
}