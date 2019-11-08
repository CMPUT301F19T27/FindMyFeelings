package com.example.findmyfeelings;

import java.io.ObjectStreamException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    /*private ArrayList<FollowUser> followersList;
    private ArrayList<FollowUser> followingList;
    private ArrayList<Mood> myMoods;
    private ArrayList<FollowUser> requestList;
    */
    private Mood recentMood;

    public User() {

    }
    public User(String email, String username, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.recentMood = new Mood();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public Mood getRecentMood() {
        return recentMood;
    }

    public void setRecentMood(Mood recentMood) {
        this.recentMood = recentMood;
    }

    public HashMap<String, Object> userToMap() {

        HashMap<String, Object> uMap = new HashMap<String, Object>();

        uMap.put("first_name", firstName);
        uMap.put("last_name", lastName);
        uMap.put("username", username);
        uMap.put("recent_mood", recentMood);

        return uMap;
    }
}
