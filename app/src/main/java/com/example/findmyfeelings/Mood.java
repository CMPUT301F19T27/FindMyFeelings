package com.example.findmyfeelings;

import java.io.Serializable;
import java.util.Date;

public class Mood implements Serializable {
    private String moodId;
    private Date dateTime;
    private String mood;
    private String reason;

//    private Location location;
//    private Photo photo;

    public Mood() {

    }

    public Mood(String moodId, Date dateTime, String mood, String reason) {
        this.moodId = moodId;
        this.dateTime = dateTime;
        this.mood = mood;
        this.reason = reason;
    }

    public String getMoodId() {
        return moodId;
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
}
