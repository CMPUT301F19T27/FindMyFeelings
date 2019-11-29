package com.example.findmyfeelings;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * contains user moods
 */
public class Mood implements Serializable, Parcelable {
    private String moodId;
    private String username;
    private Date dateTime;
    private String mood;
    private String reason;

    private String situation;


    private String imageURL;

    private GeoPoint location; // Not serializable, causes app to crash when adding a photo to an existing mood (try adding a photo to a non-location mood)

    public Mood() {
       // Empty Constructor Required
    }

    public Mood(String moodId, String username, Date dateTime, String mood, String reason, String situation, GeoPoint location) {
        this.moodId = moodId;
        this.username = username;
        this.dateTime = dateTime;
        this.mood = mood;
        this.reason = reason;
        this.situation = situation;
        this.location = location;
        this.imageURL = "";
    }

    public Mood(String moodId, String username, Date dateTime, String mood, String reason, String situation, GeoPoint location, String imageURL) {
        this.moodId = moodId;
        this.username = username;
        this.dateTime = dateTime;
        this.mood = mood;
        this.reason = reason;
        this.situation = situation;
        this.location = location;
        this.imageURL = imageURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeDouble(location.getLatitude());
        parcel.writeDouble(location.getLongitude());
    }

    public Mood(Parcel in)
    {
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        location = new GeoPoint(lat, lng);
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

    public void setMoodId(String moodId) {
        this.moodId = moodId;
    }


    public String getImageURL() {
        return imageURL;
    }
}
