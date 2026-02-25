package com.example.soundenchancement

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioBoostServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    @Before
    fun setup() {
        // Inject the fake factory before starting the service
        AudioBoostService.effectFactory = FakeAudioEffectFactory()
    }

    @After
    fun tearDown() {
        // Restore default factory after test
        AudioBoostService.effectFactory = AudioEffectFactory()
    }

    @Test
    fun serviceStartsAndInitializesEffects() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, AudioBoostService::class.java)

        val binder = serviceRule.bindService(intent)
        val service = (binder as AudioBoostService.LocalBinder).getService()

        // Service itself is created
        assertNotNull(service)

        // BassBoost
        val bassBoost = service.getBassBoost()
        assertNotNull(bassBoost)
        assertTrue(bassBoost?.roundedStrength?.toInt() ?: 0 > 0)
        assertTrue(bassBoost?.enabled ?: false)

        // LoudnessEnhancer
        val loudness = service.getLoudnessEnhancer()
        assertNotNull(loudness)
        assertEquals(1000, loudness?.targetGain)
        assertTrue(loudness?.enabled ?: false)
    }
}