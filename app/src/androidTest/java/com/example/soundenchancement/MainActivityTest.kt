package com.example.soundenchancement

import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun onLaunch_statusShouldShowOn() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val statusLabel = activity.findViewById<TextView>(R.id.statusLabel)
                assertEquals("Sound effect: ON", statusLabel.text.toString())
            }
        }
    }

    @Test
    fun onLaunch_stopButtonShouldTurnOffStatus() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // Simulate button click directly on the UI thread
                activity.findViewById<android.widget.Button>(R.id.btnStop).performClick()

                val statusLabel = activity.findViewById<TextView>(R.id.statusLabel)
                assertEquals("Sound effect: OFF", statusLabel.text.toString())
            }
        }
    }
}