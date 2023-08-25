package com.example.timetracker;

import java.time.LocalDate;

public class DailyTimeSummary {
    private String day;
    private long totalMillies;

    public DailyTimeSummary(String day, long totalMillies) {
        this.day = day;
        this.totalMillies = totalMillies;
    }

    public String getDay() {
        return day;
    }

    public long getTotalMillies() {
        return totalMillies;
    }

    @Override
    public String toString() {
        return "DailyTimeSummary{" +
                "day='" + day + '\'' +
                ", totalMillies=" + totalMillies +
                '}';
    }
}
