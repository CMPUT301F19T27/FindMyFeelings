package com.example.findmyfeelings;

public class Mood {
    private int dateDay;
    private int dateMonth;
    private int dateYear;

    private int timeMinute;
    private int timeHour;

    private String mood;

    private String reason;

//    private Location location;
//    private Photo photo;

    Mood(int dateDay, int dateMonth, int dateYear, int timeMinute, int timeHour, String mood, String reason) {
        this.dateDay = dateDay;
        this.dateMonth = dateMonth;
        this.dateYear = dateYear;

        this.timeMinute = timeMinute;
        this.timeHour = timeHour;

        this.mood = mood;

        this.reason = reason;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }
    
    public String getDateString() {
        return this.dateDay + "/" + this.dateMonth + "/" + this.dateYear;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public String getTimeString() {
        return this.timeHour + ":" + this.timeMinute;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
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
