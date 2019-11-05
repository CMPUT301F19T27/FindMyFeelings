package com.example.findmyfeelings;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Mood implements Serializable {
    private Date dateTime;
    private String mood;
    private String reason;

//    private Location location;
//    private Photo photo;

    public Mood() {

    }

    public Mood(Date dateTime, String mood, String reason) {
        this.dateTime = dateTime;
        this.mood = mood;
        this.reason = reason;
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

    public int getEmoji() {
        int moodImage;

        switch(mood) {
            case "Happy":
                moodImage = R.drawable.happy_face;
                break;
            case "Angry":
                moodImage = R.drawable.angry_face;
                break;
            case "Disgusted":
                moodImage = R.drawable.disgust_face;
                break;
            case "Scared":
                moodImage = R.drawable.fear_face;
                break;
            case "Sad":
                moodImage = R.drawable.sad_face;
                break;
            case "Surprised":
                moodImage = R.drawable.surprised_face;
                break;
            default:
                moodImage = R.drawable.null_face;
        }

        return moodImage;
    }
}
