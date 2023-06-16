package com.example.timetracker;

public class ActivityEntry {
    public ActivityEntry(float time, int day, String activity) {
        this.time = time;
        this.day = day;
        this.activity = activity;
    }

    private float time;
    private int day;
    private String activity;


    public float getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public String getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return "ActivityEntry{" +
                "time=" + time +
                ", day=" + day +
                ", activity='" + activity + '\'' +
                '}';
    }
}
