package com.example.findmyfeelings;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ServiceTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
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


    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

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
     * MUST BE LOGGED IN TO WORK/ MUST ALSO START OUT WITH EMPTY MOODS
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
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.happy_emoticon));
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Mood Test");

        View situation_drop = solo.getView(R.id.situation_selector, 0);
        solo.clickOnView(situation_drop); // Clicks on the arrow to display situation
        solo.clickOnText("With Someone");

        solo.isSpinnerTextSelected("With Someone"); // Check if Spinner was selected


        solo.clickOnCheckBox(1); // Checks Custom Date


        solo.clickOnButton("OK");

        //Check if the Mood was added to the list
        assertTrue(solo.waitForText("Happy", 1, 5000));

        //Delete Mood
        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.waitForDialogToOpen(2000);
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

    @Test
    public void checkMoodScroll(){
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);


        //perform adding a Mood
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);

        HorizontalScrollView moodScroll = (HorizontalScrollView) solo.getView(R.id.Mood_scroll);
        moodScroll.scrollTo(200, 0);



        solo.clickOnView(solo.getView(R.id.scared_emoticon));

        solo.clickOnButton("OK");

        //Check if the Mood was added to the list
        assertTrue(solo.waitForText("Scared", 1, 5000));

        //Delete Mood
        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.waitForDialogToOpen(2000);
        solo.clickOnButton("DELETE");

        //Check if the Mood is Deleted
        assertFalse(solo.searchText("Scared", true));

    }


    @Test
    public void checkMoodFilter() {
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);

        // Happy
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.happy_emoticon));

        solo.clickOnButton("OK");

        //Check if the Happy was added
        assertTrue(solo.waitForText("Happy", 1, 5000));

        // Sad
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.sad_emoticon));

        solo.clickOnButton("OK");

        //Check if the Sad
        assertTrue(solo.waitForText("Sad", 1, 5000));

        // Angry
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.angry_emoticon));

        solo.clickOnButton("OK");

        //Check if the Angry was added
        assertTrue(solo.waitForText("Angry", 1, 5000));

        // Disgusted
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.disgusted_emoticon));

        solo.clickOnButton("OK");

        //Check if Disgusted was added
        assertTrue(solo.waitForText("Disgusted", 1, 5000));

        // Scared
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);

        HorizontalScrollView moodScroll = (HorizontalScrollView) solo.getView(R.id.Mood_scroll);
        moodScroll.scrollTo(200, 0);

        solo.clickOnView(solo.getView(R.id.scared_emoticon));

        solo.clickOnButton("OK");

        //Check if the Mood was added to the list
        assertTrue(solo.waitForText("Scared", 1, 5000));

        // Surprised
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.waitForDialogToOpen(2000);
        solo.clickOnView(solo.getView(R.id.surprised_emoticon));

        solo.clickOnButton("OK");

        //Check if Surprised was added
        assertTrue(solo.waitForText("Surprised", 1, 5000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Happy filter
        solo.clickOnView(solo.getView(R.id.happy_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertTrue(solo.waitForText("Happy", 1, 5000));
        assertFalse(solo.waitForText("Sad", 1, 1000));
        assertFalse(solo.waitForText("Angry", 1, 1000));
        assertFalse(solo.waitForText("Disgusted", 1, 1000));
        assertFalse(solo.waitForText("Surprised", 1, 1000));
        assertFalse(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Sad filter
        solo.clickOnView(solo.getView(R.id.sad_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertFalse(solo.waitForText("Happy", 1, 5000));
        assertTrue(solo.waitForText("Sad", 1, 1000));
        assertFalse(solo.waitForText("Angry", 1, 1000));
        assertFalse(solo.waitForText("Disgusted", 1, 1000));
        assertFalse(solo.waitForText("Surprised", 1, 1000));
        assertFalse(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Angry filter
        solo.clickOnView(solo.getView(R.id.angry_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertFalse(solo.waitForText("Happy", 1, 5000));
        assertFalse(solo.waitForText("Sad", 1, 1000));
        assertTrue(solo.waitForText("Angry", 1, 1000));
        assertFalse(solo.waitForText("Disgusted", 1, 1000));
        assertFalse(solo.waitForText("Surprised", 1, 1000));
        assertFalse(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Disgusted filter
        solo.clickOnView(solo.getView(R.id.disgusted_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertFalse(solo.waitForText("Happy", 1, 5000));
        assertFalse(solo.waitForText("Sad", 1, 1000));
        assertFalse(solo.waitForText("Angry", 1, 1000));
        assertTrue(solo.waitForText("Disgusted", 1, 1000));
        assertFalse(solo.waitForText("Surprised", 1, 1000));
        assertFalse(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Scared filter
        solo.clickOnView(solo.getView(R.id.scared_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertFalse(solo.waitForText("Happy", 1, 5000));
        assertFalse(solo.waitForText("Sad", 1, 1000));
        assertFalse(solo.waitForText("Angry", 1, 1000));
        assertFalse(solo.waitForText("Disgusted", 1, 1000));
        assertFalse(solo.waitForText("Surprised", 1, 1000));
        assertTrue(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Select Surprised filter
        solo.clickOnView(solo.getView(R.id.surprised_emoticon));
        solo.clickOnButton("OK");

        // Check if it filtered
        assertFalse(solo.waitForText("Happy", 1, 5000));
        assertFalse(solo.waitForText("Sad", 1, 1000));
        assertFalse(solo.waitForText("Angry", 1, 1000));
        assertFalse(solo.waitForText("Disgusted", 1, 1000));
        assertTrue(solo.waitForText("Surprised", 1, 1000));
        assertFalse(solo.waitForText("Scared", 1, 1000));


        // Open Filter Menu
        solo.clickOnView(solo.getView(R.id.filter_button));

        // Remove filters
        solo.clickOnView(solo.getView(R.id.remove_filters_button));
        solo.clickOnButton("OK");

        solo.waitForDialogToClose(2000);

        // Check if it filtered
//        assertTrue(solo.waitForText("Happy", 1, 5000));
//        assertTrue(solo.waitForText("Sad", 1, 1000));
//        assertTrue(solo.waitForText("Angry", 1, 1000));
//        assertTrue(solo.waitForText("Disgusted", 1, 1000));
//        assertTrue(solo.waitForText("Surprised", 1, 1000));
//        assertTrue(solo.waitForText("Scared", 1, 1000));

        // Clean up the moods
        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");

        solo.clickOnView(solo.getView(R.id.mood_emoticon));
        solo.clickOnButton("DELETE");
    }


    /**
     * Closes the activity after each test
     */

    @After
    public void tearDown(){
        solo.finishOpenedActivities();


    }


}