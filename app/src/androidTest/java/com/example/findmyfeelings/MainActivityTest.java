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
     * Check if Login is valid
     * Will fail upon auto-login
     */
    /*
    @Test
    public void checkLogin(){
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);


        //perform Login
        solo.enterText((EditText)solo.getView(R.id.username_editText), "test@gmail.com");

        //solo.clickOnCheckBox(R.id.location_check);
        solo.clickOnButton(R.id.login_button);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);



    }

*/




    /**
     * MUST BE LOGGED IN TO WORK
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
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        //solo.clickOnCheckBox(R.id.location_check);
        solo.clickOnButton("OK");

        //Check if the Mood was added to the list
        assertTrue(solo.waitForText("Happy", 1, 5000));

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

    @Test
    public void checkMoodFilter() {
        // Check if Login Page Starts
        solo.assertCurrentActivity("Should be MainActivity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);


        // Add a few moods
        // Happy
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:48");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Happy");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");

        // Sad
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:49");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Sad");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");

        // Angry
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:50");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Angry");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");

        // Disgusted
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:51");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Disgusted");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");

        // Scared
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:52");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Scared");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");

        // Surprised
        solo.clickOnView(solo.getView(R.id.add_mood_button));
        solo.enterText((EditText)solo.getView(R.id.mood_date_editText), "2019-11-12");
        solo.enterText((EditText)solo.getView(R.id.mood_time_editText), "18:53");
        solo.enterText((EditText)solo.getView(R.id.mood_type_editText), "Surprised");
        solo.enterText((EditText)solo.getView(R.id.mood_reason_editText), "Testing");
        solo.enterText((EditText)solo.getView(R.id.mood_situation_editText), "Alone");

        solo.clickOnButton("OK");


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
        solo.clickOnView(solo.getView(R.id.happy_emoticon));
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

        // Check if it filtered
        assertTrue(solo.waitForText("Happy", 1, 5000));
        assertTrue(solo.waitForText("Sad", 1, 1000));
        assertTrue(solo.waitForText("Angry", 1, 1000));
        assertTrue(solo.waitForText("Disgusted", 1, 1000));
        assertTrue(solo.waitForText("Surprised", 1, 1000));
        assertTrue(solo.waitForText("Scared", 1, 1000));

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