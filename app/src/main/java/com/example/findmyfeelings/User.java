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
    private ArrayList<Follower> followersList;
    private ArrayList<Following> followingList;
    private ArrayList<Mood> myMoods;
    private ArrayList<String> requestList;
    private Mood recentMood;

    public User(String email, String username, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.followersList = new ArrayList<Follower>();
        this.followingList = new ArrayList<Following>();
        this.myMoods = new ArrayList<Mood>();
        this.requestList = new ArrayList<String>();
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

    public ArrayList<Follower> getFollowersList() {
        return followersList;
    }

    public ArrayList<Following> getFollowingList() {
        return followingList;
    }

    public ArrayList<String> getRequestList() {
        return requestList;
    }

    public ArrayList<Mood> getMyMoods() {
        return myMoods;
    }

    public Mood getRecentMood() {
        return recentMood;
    }

    /**
     * adds a user to followersList
     * @param fUser
     */
    public void addFollower(Follower fUser) {
        followersList.add(fUser);
    }

    /**
     * adds a user to followingList
     * @param fUser
     */
    public void addFollowing(Following fUser) {
        followingList.add(fUser);
    }

    /**
     * removes a user from followerList
     * @param fUser
     */
    public void removeFollower(Follower fUser) {
        followersList.remove(fUser);
    }

    /**
     * removes a user from followingList
     * @param fUser
     */
    public void removeFollowing(Following fUser) {
        followingList.remove(fUser);
    }

    /**
     * adds a new user request to requestList
     * @param rEmail
     */
    public void addRequest(String rEmail) {
        requestList.add(rEmail);
    }

    /**
     * removes a user request from requestList
     * @param rEmail
     */
    public void removeRequest(String rEmail) {
        requestList.remove(rEmail);
    }

    public void addMood(Mood newMood) {
        recentMood = newMood;
        myMoods.add(newMood);

    }

    public HashMap<String, Object> userToMap() {

        HashMap<String, Object> uMap = new HashMap<String, Object>();

        uMap.put("first_name", firstName);
        uMap.put("last_name", lastName);
        uMap.put("username", username);
        uMap.put("followers", followersList);
        uMap.put("following", followingList);
        uMap.put("requests", requestList);
        uMap.put("recent_mood", recentMood);
        uMap.put("my_moods", myMoods);

        return uMap;
    }
}
