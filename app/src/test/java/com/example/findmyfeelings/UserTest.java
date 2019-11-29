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
 * Test class for User
 */
public class UserTest {


    private ArrayList<User> mockUserList(){
        ArrayList<User> mockUserList = new ArrayList<>();
        mockUserList.add(mockUser());
        return mockUserList;


    }

    private User mockUser(){
        User mockUser = new User("mock@gmail.com", "mock", "mockFirst", "mockLast");
        Mood mood = new Mood("1", "test", new Date(2012/12/15), "Happy", "none", "alone", new GeoPoint(24,2));
        mockUser.setRecentMood(mood);
        return mockUser;


    }


    @Test
    public void testAdd(){
        ArrayList<User> userList= mockUserList();
        assertEquals(1, userList.size()); // Should have 1 User
        assertTrue(userList.get(0).getEmail().contains("mock@gmail.com")); // Test if mock@gmail.com is in list

        User user = new User("mock2@gmail.com", "mock2", "mock2First", "mock2Last");

        userList.add(user); // List is size 2

        assertEquals(2, userList.size());

        assertTrue(userList.get(1).getUsername().contains("mock2")); // Test if new user was added

    }


    @Test
    public void testGetUserAttributes(){
        ArrayList<User> userList= mockUserList();

        assertTrue(userList.get(0).getEmail().contains("mock@gmail.com"));

        assertTrue(userList.get(0).getUsername().contains("mock"));

        assertTrue(userList.get(0).getFirstName().contains("mockFirst"));
        assertTrue(userList.get(0).getLastName().contains("mockLast"));

        Mood mockMood = new Mood("1", "test", new Date(2012/12/15), "Happy", "none", "alone", new GeoPoint(24,2));

        Mood mood = userList.get(0).getRecentMood();

        assertEquals(mood.getMoodId(), mockMood.getMoodId());


    }


    @Test
    public void testDelete(){

        ArrayList<User> userList= mockUserList();
        assertEquals(1, userList.size()); // Should have 1 mood

        userList.remove(0);

        assertEquals(0, userList.size()); // List is empty


    }




}

