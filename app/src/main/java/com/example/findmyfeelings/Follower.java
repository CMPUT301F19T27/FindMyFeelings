package com.example.findmyfeelings;

public class Follower {

    private String email;
    private Mood recentMood;

    public Follower() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Mood getRecentMood() {
        return recentMood;
    }

    public void setRecentMood(Mood recentMood) {
        this.recentMood = recentMood;
    }
}
