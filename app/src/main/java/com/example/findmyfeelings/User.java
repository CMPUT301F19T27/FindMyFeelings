package com.example.findmyfeelings;

import java.io.ObjectStreamException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class User {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private ArrayList<String> followersList;
    private ArrayList<String> followingList;
    private ArrayList<Mood> myMoods;
    private ArrayList<String> requestList;
    private Mood recentMood;

    public User() {

    }
    public User(String email, String username, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.followersList = new ArrayList<String>();
        this.followingList = new ArrayList<String >();
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

    public ArrayList<String > getFollowersList() {
        return followersList;
    }

    public ArrayList<String > getFollowingList() {
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

    public void setFollowersList(ArrayList<String> followersList) {
        this.followersList = followersList;
    }

    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

    public void setMyMoods(ArrayList<Mood> myMoods) {
        this.myMoods = myMoods;
    }

    public void setRequestList(ArrayList<String> requestList) {
        this.requestList = requestList;
    }

    public void setRecentMood(Mood recentMood) {
        this.recentMood = recentMood;
    }

    /**
     * adds a user to followersList
     * @param fUser
     */
    public void addFollower(String  fUser) {
        followersList.add(fUser);
    }

    /**
     * adds a user to followingList
     * @param fUser
     */
    public void addFollowing(String  fUser) {
        followingList.add(fUser);
    }

    /**
     * removes a user from followerList
     * @param fUser
     */
    public void removeFollower(String fUser) {
        followersList.remove(fUser);
    }

    /**
     * removes a user from followingList
     * @param fUser
     */
    public void removeFollowing(String fUser) {
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
