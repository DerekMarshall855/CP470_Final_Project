package com.example.cp470_final_project;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.Button;

import androidx.test.rule.ActivityTestRule;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity = null;
    private LevelSelect lActivity = null;

    Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(HelpActivity.class.getName(), null, false);

    @Before
    public void setUp() {
        mActivity = mActivityActivityTestRule.getActivity();

    }

    @Test
    public void testHelpButton(){
        assertNotNull(mActivity.findViewById(R.id.Help));

        onView(withId(R.id.Help)).perform(click());

        Activity helpActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(helpActivity);
    }


    @Test
    public void testSettingsButton(){
        Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
        assertNotNull(mActivity.findViewById(R.id.Settings));

        onView(withId(R.id.Settings)).perform(click());

        Activity settingsActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(settingsActivity);
    }

    @Test
    public void testPlayButton(){
        Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(LevelSelect.class.getName(), null, false);
        assertNotNull(mActivity.findViewById(R.id.Play));

        onView(withId(R.id.Play)).perform(click());

        Activity playActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(playActivity);
    }



    @After
    public void tearDown() {
        mActivity = null;
    }

}