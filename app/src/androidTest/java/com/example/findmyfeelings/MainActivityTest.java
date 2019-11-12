package com.example.findmyfeelings;


import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 * used
 */
@RunWith(AndroidJUnit4.class)

public class MainActivityTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>
            (MainActivity.class, true, true);

    /**
     * Runs before all tests and creates a solo instance
     *
     */

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());


    }

    /**
     * Gets the Activity
     */

    @Test
    public void start(){
        Activity activity = rule.getActivity();

    }

    /**
     * Add a mood to the MoodCustomList and check the mood name using assertTrue
     * Delete the mood from the MoodCustomList and check again with assertFalse
     */

    @Test
    public void checkMoodCustomList(){
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);


        //perform adding a Mood
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2012-06-14");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "11:34");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Happy");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");

        //solo.clickOnCheckBox(R.id.location_check);
        solo.clickOnButton("OK");

        //Check if the Mood was added to the list
        assertTrue(solo.waitForText("Happy", 1, 2000));

        //Delete Mood
        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        //Check if the Mood is Deleted
        assertFalse(solo.searchText("Happy", true));

    }


    /**
     * Check if activity switches upon clicking bottom navigation bar
     * We are checking if the bottom bar cycles through map, home, & profile.
     */

    @Test
    public void checkActivitySwitch(){
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);

        // Goto Map
        solo.clickOnView(solo.getView(R.id.ic_map));

        // Check if we are on MapActivity
        solo.assertCurrentActivity("Should be MapActivity", MapActivity.class);

        // Goto Profile
        solo.clickOnView(solo.getView(R.id.ic_profile));

        // Check if we are on ProfileActivity
        solo.assertCurrentActivity("Should be ProfileActivity", ProfileActivity.class);

        // Goto HomePageActivity
        solo.clickOnView(solo.getView(R.id.ic_feed));

        // Check if we are on HomePageActivity
        solo.assertCurrentActivity("Should be HomePageActivity", HomePageActivity.class);

    }

    /**
     * Closes the activity after each test
     */

    @After
    public void tearDown(){
        solo.finishOpenedActivities();


    }


}