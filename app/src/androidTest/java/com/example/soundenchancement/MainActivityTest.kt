package com.example.soundenchancement

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun onLaunch_statusShouldShowOn() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // UI should immediately reflect ON state
            onView(withId(R.id.statusLabel))
                .check(matches(withText("Sound effect: ON")))
        }
    }

    @Test
    fun onLaunch_stopButtonShouldTurnOffStatus() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Press OFF button
            onView(withId(R.id.btnStop)).perform(androidx.test.espresso.action.ViewActions.click())

            // Status should update to OFF
            onView(withId(R.id.statusLabel))
                .check(matches(withText("Sound effect: OFF")))
        }
    }
}