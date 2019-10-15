package com.snc.farmaccount


import android.net.sip.SipSession
import android.service.autofill.FieldClassification
import android.view.View.VISIBLE

import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.snc.farmaccount.event.AddEventFragment
import com.snc.farmaccount.event.TagAdapter
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule
import java.lang.Exception



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.snc.farmaccount", appContext.packageName)
    }


    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listGoesOverTheFold() {
        pauseTestFor(2000)
        onView(withId(R.id.image_add_event))
            .perform(click())

        pauseTestFor(2000)
        onView(withId(R.id.edit_expand))
            .perform(click())

        pauseTestFor(2000)
        onView(withId(R.id.edit_expand))
            .perform(typeText("22"), closeSoftKeyboard())

        pauseTestFor(2000)
        onView(withId(R.id.tag_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        pauseTestFor(2000)
        onView(withId(R.id.edit_description))
            .perform(typeText("hmm yummy yummy"), closeSoftKeyboard())

        pauseTestFor(2000)
        onView(withId(R.id.image_save))
            .perform(click())

        pauseTestFor(4000)
        onView(allOf(withId(R.id.event_list),isDisplayed())).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        pauseTestFor(2000)

    }

    private fun pauseTestFor(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }
}
