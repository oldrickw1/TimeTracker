package com.example.timetracker;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class ActivityEntry {
    private float time;
    private ZonedDateTime date;
    private String activity;

    public ActivityEntry(float time, ZonedDateTime date, String activity) {
        this.time = time;
        this.date = date;
        this.activity = activity;
    }



    public float getTime() {
        return time;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public float getHoursAndMinutes() {
        int hours = (int) (time / 60);
        int minutes = (int) (time % 60);
        String hoursAndMinutes = hours + "." + minutes;
        return Float.parseFloat(hoursAndMinutes);
    }

    @Override
    public String toString() {
        return "ActivityEntry{" +
                "time=" + time +
                ", day=" + date +
                ", activity='" + activity + '\'' +
                '}';
    }
}
