package com.example.soundenchancement

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class AudioBoostServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    @Test
    fun service_startsSuccessfully() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val intent = Intent(context, AudioBoostService::class.java)

        val binder = serviceRule.startService(intent)

        assertNotNull(binder)
    }
}