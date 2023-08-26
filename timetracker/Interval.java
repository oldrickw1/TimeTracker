package com.example.timetracker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Interval {
    private long startTimeMillis;
    private long endTimeMillis;
    private long durationTimeMillis;
    private ZonedDateTime zonedDateTime;
    private String activity;

    public Interval(long startTimeMillis, long endTimeMillis, String activity) {
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.durationTimeMillis = endTimeMillis - startTimeMillis;
        this.zonedDateTime = getZonedDateTimeFromEpochMillis(endTimeMillis); // endTime of interval is determining for which date interval belongs to.
        this.activity = activity;
    }

    private ZonedDateTime getZonedDateTimeFromEpochMillis(long endTimeMillis) {
        Instant instant = Instant.ofEpochMilli(endTimeMillis);
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public long getDurationTimeMillis() {
        return durationTimeMillis;
    }

    public double getDurationTimeSeconds() {
        return (double) durationTimeMillis / 1000;
    }

    public double getDurationTimeMinutes() {
        return (double) durationTimeMillis / 1000 / 60;
    }

    public double getDurationTimeHours() {
        return (double) durationTimeMillis / 1000 / 60 / 60;
    }


    public String getActivity() {
        return activity;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public float getHoursAndMinutes() {
        int hours = (int) (durationTimeMillis / 1000 / 60);
        int minutes = (int) ((durationTimeMillis / 1000)% 60);
        String hoursAndMinutes = hours + "." + minutes;
        return Float.parseFloat(hoursAndMinutes);
    }

    @Override
    public String toString() {
        return "Interval{ durationTimeMillis=" + durationTimeMillis +
                "durationTimeMinutes=" + getDurationTimeMinutes();
    }

    public static Map<LocalDate, Double> groupAndCalculateTotalTimeByDate(List<Interval> intervals) {
        Map<LocalDate, List<Interval>> groupedIntervals = intervals.stream()
                .collect(Collectors.groupingBy(interval -> interval.getZonedDateTime().toLocalDate()));

        Map<LocalDate, Double> totalTimeByDate = new HashMap<>();
        groupedIntervals.forEach((date, dateIntervals) -> {
            double totalMinutes = dateIntervals.stream()
                    .mapToDouble(Interval::getDurationTimeMinutes)
                    .sum();
            totalTimeByDate.put(date, totalMinutes);
        });

        return totalTimeByDate;
    }

}

