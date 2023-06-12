package com.example.timetracker;

public class ActivityEntry {
    public ActivityEntry(int time, int day, String activity) {
        this.time = time;
        this.day = day;
        this.activity = activity;
    }

    private int time;
    private int day;
    private String activity;


    public int getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public String getActivity() {
        return activity;
    }

}
