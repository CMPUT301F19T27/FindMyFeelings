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
        return new Mood("1", "test", new Date(), "Happy", "none", "alone", new GeoPoint(24,2));


    }


    @Test
    public void testAdd(){
        ArrayList<Mood> moodList= mockMoodList();
        assertEquals(1, moodList.size()); // Should have 1 mood
        assertTrue(moodList.get(0).getMood().contains("Happy")); // Test if Sad is in list

        Mood mood = new Mood("2", "test1", new Date(), "Sad", "none", "alone", new GeoPoint(24,2));
        moodList.add(mood); // List is size 2

        assertEquals(2, moodList.size());

        assertTrue(moodList.get(1).getMood().contains("Sad")); // Test if Sad is in list

    }

/*
    @Test
    public void testgetMoodId(){
        CityList cityList = mockCityList();

        assertEquals(0,mockCity().compareTo(cityList.getCities().get(0))); // Comparing Edmonton == Edmonton

        City city = new City ("Charlottetown", "Prince Edward Island");
        cityList.add(city);

        assertEquals(0,city.compareTo(cityList.getCities().get(0))); // Charlottetown == Charlottetown
        assertEquals(0,mockCity().compareTo(cityList.getCities().get(1))); // Edmonton == Edmonton

    }

    @Test
    public void testHasCity(){
        CityList cityList = mockCityList();

        assertEquals(0,mockCity().compareTo(cityList.getCities().get(0))); // Comparing Edmonton == Edmonton
        assertEquals(1, cityList.getCities().size()); //Edmonton should in in list

        assertTrue((cityList.hasCity(cityList.getCities().get(0)))); // Check for Edmonton

        City city = new City("Vancouver","BC");
        cityList.add(city);
        assertTrue((cityList.hasCity(city))); // Check for BC

        City cityFake = new City("Wall","Street");
        assertFalse((cityList.hasCity(cityFake))); // Check for Wall Street


    }


    @Test
    public void testDelete(){
        CityList cityList = mockCityList();

        assertEquals(1, cityList.getCities().size()); //Edmonton should in in list

        cityList.delete(cityList.getCities().get(0)); // Removing Edmonton from list

        assertEquals(0, cityList.getCities().size()); // List is empty

        City city = new City("Vancouver","BC");
        City city2 = new City("Wall","Street");
        cityList.add(city); // add Vancouver
        cityList.add(city2); // add Wall

        assertEquals(2, cityList.getCities().size()); // Size should be 2

        cityList.delete(city); // Deleting Vancouver

        assertEquals(1, cityList.getCities().size()); // Size should be 1

        assertFalse(cityList.hasCity(city)); // Check if Vancouver is still in list should return false



    }


    @Test
    public void testDeleteException(){
        final CityList cityList = mockCityList();

        City city = new City("Yellowknife","Northwest Territories");

        cityList.add(city);

        assertEquals(2, cityList.getCities().size()); // Edmonton, Yellowknife

        cityList.delete(city); // Delete Yellowknife

        assertEquals(1, cityList.getCities().size()); // Edmonton

        assertThrows(IllegalArgumentException.class, ()->{
            cityList.delete(city);  // throw exception if we try to delete Yellowknife again

        });

        assertFalse(cityList.hasCity(city)); // check if Yellowknife still exists (should not be!)

    }

    @Test
    public void testCountCities(){
        CityList cityList = mockCityList();

        assertEquals(1, cityList.countCities()); // 1 == 1

        City city = new City ("Galaxy", "Land");
        cityList.add(city); // size = 2

        assertEquals(2, cityList.countCities()); // 2 == 2

        cityList.delete(city);
        assertEquals(1, cityList.countCities()); // 1 == 1

        cityList.delete(cityList.getCities().get(0));

        assertEquals(0, cityList.countCities()); // 0 == 0

    }


*/


}
