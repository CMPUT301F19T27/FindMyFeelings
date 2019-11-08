package com.example.findmyfeelings;

import android.widget.ImageView;
import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * contains user moods
 */
public class Mood implements Serializable {
    private String moodId;
    private String username;
    private Date dateTime;
    private String mood;
    private String reason;
    private String situation;
    private GeoPoint location;

//    private Location location;
//    private Photo photo;

    public Mood() {

    }

    public Mood(String moodId, String username, Date dateTime, String mood, String reason, String situation, GeoPoint location) {
        this.moodId = moodId;
        this.username = username;
        this.dateTime = dateTime;
        this.mood = mood;
        this.reason = reason;
        this.situation = situation;
        this.location = location;
    }

    public String getMoodId() {
        return moodId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;

    }
}
