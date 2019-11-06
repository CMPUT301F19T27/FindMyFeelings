package com.example.findmyfeelings;


import android.app.Activity;
import android.view.View;
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
     * @throws
     */

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }

    /**
     * Add a mood to the MoodCustomList and check the mood name using assertTrue
     * Clear all the Moods from the MoodCustomList and check again with assertFalse
     */

    @Test
    public void checkMoodCustomList(){
        // Check if Login Page Starts
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Wait for Home Page to Load
        solo.waitForActivity(HomePageActivity.class, 3000);
        //solo.waitForActivity(HomePageActivity.class);

       // solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);

        //perform adding a Mood
        //solo.clickOnButton("ADD MOOD");
        //solo.enterText((EditText)solo.getView(R.id.editText_name), "Edmonton");
        //solo.clickOnButton("OK");
        //solo.clearEditText((EditText)solo.getView(R.id.editText_name)); //clear edit text

        //assert the city shown
        //assertTrue(solo.waitForText("Happy", 1, 2000));

        //solo.clickOnButton("ClEAR ALL");

        //assert that the city is gone
        //assertFalse(solo.searchText("Edmonton"));

    }

    /**
     * Check item taken from the listview
     */

    /**
    @Test
    public void checkCityListItem(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //perform adding a city
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText)solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.clearEditText((EditText)solo.getView(R.id.editText_name)); //clear edit text

        //assert the city shown
        assertTrue(solo.waitForText("Edmonton", 1, 2000));

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList;
        String city = (String) cityList.getItemAtPosition(0);
        assertEquals(city, "Edmonton");



    }

    /**
     * Check if activity switched
     */

    /**
    @Test
    public void checkActivitySwitch(){
        //perform adding a city
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText)solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.clearEditText((EditText)solo.getView(R.id.editText_name)); //clear edit text

        //assert the city shown
        assertTrue(solo.waitForText("Edmonton", 1, 2000));

        //Get view object
        View view = solo.getCurrentActivity().findViewById(R.id.content_view);
        assertNotNull(view); // check if there is an entry
        solo.clickOnView(view); // Click on Edmonton

        // Check if We are on ShowActivity after click
        // if activity != solo.getCurrentActivity then we have switched
        solo.assertCurrentActivity("Wrong Activity", ShowActivity.class);

        //assert the city shown, check if name is consistent
        assertTrue(solo.waitForText("Edmonton", 1, 3000));

        // check back Button
        solo.clickOnButton("Back");

        // Check if we are on Main Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);


    }




    /**
     * Closes the activity after each test
     */

    @After
    public void tearDown(){
        solo.finishOpenedActivities();


    }


}