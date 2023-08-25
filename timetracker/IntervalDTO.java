package com.example.timetracker;

import java.time.ZonedDateTime;

public class IntervalDTO {
    private float minutes;
    private ZonedDateTime date;
    private String activity;

    public IntervalDTO(float minutes, ZonedDateTime date, String activity) {
        this.minutes = minutes;
        this.date = date;
        this.activity = activity;
    }

    public float getMinutes() {
        return minutes;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getActivity() {
        return activity;
    }

    public float getHoursAndMinutes() {
        int hours = (int) (minutes / 60);
        int minutes = (int) (this.minutes % 60);
        String hoursAndMinutes = hours + "." + minutes;
        return Float.parseFloat(hoursAndMinutes);
    }

    @Override
    public String toString() {
        return "ActivityEntry{" +
                "time=" + minutes + " Hours" +
                ", day=" + date +
                ", activity='" + activity + '\'' +
                '}';
    }
}
