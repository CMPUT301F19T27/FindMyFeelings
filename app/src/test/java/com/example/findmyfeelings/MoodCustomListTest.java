package com.example.simpleparadox.listycity;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MoodCustomListTest{
/*
    private CityList mockCityList(){
        CityList cityList = new CityList();
        cityList.add(mockCity());
        return cityList;


    }

    private City mockCity(){
        return new City("Edmonton","Alberta");


    }


    @Test
    public void testAdd(){
        CityList cityList = mockCityList();
        assertEquals(1, cityList.getCities().size()); //Edmonton should in in list

        City city = new City("Regina", "Saskatchewan");
        cityList.add(city); // List is size 2

        assertEquals(2, cityList.getCities().size());

        assertTrue(cityList.getCities().contains(city)); // test if Regina is in the list

    }


    @Test
    public void testAddException(){
        final CityList cityList = mockCityList();

        City city = new City("Yellowknife","Northwest Territories");
        cityList.add(city);

        assertThrows(IllegalArgumentException.class, ()->{ //if valid city it will pass
            cityList.add(city);                             // else !valid city then fail

        });

    }

    @Test
    public void testGetCities(){
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
