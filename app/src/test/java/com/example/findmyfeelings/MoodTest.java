package com.example.findmyfeelings;

import com.example.findmyfeelings.Mood;
import com.example.findmyfeelings.MoodCustomList;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * Test class for Mood: add, delete, edit!
 */
public class MoodTest {


    private ArrayList<Mood> mockMoodList(){
        ArrayList<Mood> moodList = new ArrayList<>();
        moodList.add(mockMood());
        return moodList;


    }

    private Mood mockMood(){
        return new Mood("1", "test", new Date(2012/12/15), "Happy", "none", "alone" ,new GeoPoint(24,2), "Dog.png");


    }


    @Test
    public void testAdd(){
        ArrayList<Mood> moodList= mockMoodList();
        assertEquals(1, moodList.size()); // Should have 1 mood
        assertTrue(moodList.get(0).getMood().contains("Happy")); // Test if Sad is in list

        Mood mood = new Mood("1", "test", new Date(2012/12/15), "Sad", "none", "alone" ,new GeoPoint(24,2), "Dog.png");
        moodList.add(mood); // List is size 2

        assertEquals(2, moodList.size());

        assertTrue(moodList.get(1).getMood().contains("Sad")); // Test if Sad is in list

    }


    @Test
    public void testGetMoodAttributes(){
        ArrayList<Mood> moodList= mockMoodList();
        Date date = new Date(2012/12/15);
        GeoPoint geopoint = new GeoPoint(24,2);

        assertTrue(moodList.get(0).getMoodId().contains("1"));

        assertTrue(moodList.get(0).getUsername().contains("test"));
        assertEquals(date,moodList.get(0).getDateTime());

        assertTrue(moodList.get(0).getReason().contains("none"));
        assertTrue(moodList.get(0).getSituation().contains("alone"));

        assertEquals(geopoint,moodList.get(0).getLocation());


    }


    @Test
    public void testDelete(){

        ArrayList<Mood> moodList= mockMoodList();
        assertEquals(1, moodList.size()); // Should have 1 mood

        moodList.remove(0);

        assertEquals(0, moodList.size()); // List is empty


    }




}
