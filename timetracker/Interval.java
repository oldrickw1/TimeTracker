package com.example.timetracker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public static long getTotalTimeFromListOfIntervals(List<Interval> intervals) {
        long totalTimeInMillis = 0;
        for (Interval interval : intervals) {
            totalTimeInMillis += interval.getDurationTimeMillis();
        }
        return totalTimeInMillis;
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

    public long getDurationTimeMinutes() {
        return (long) durationTimeMillis / 1000 / 60;
    }

    public double getDurationTimeHours() {
        return (double) durationTimeMillis / 1000 / 60 / 60;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public String getActivity() {
        return activity;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public static String getHoursAndMinutes(long timeMillis) {
        int hours = (int) (timeMillis / 1000 / 60 / 60);
        int minutes = (int) ((timeMillis / 1000 / 60)% 60);
        return String.format(Locale.getDefault(), "%02dh:%02dm", hours, minutes);
    }



    public static List<Float> calculateTotalMinutesByDate(LocalDate start, LocalDate end, List<Interval> intervals) {
        Map<Integer, List<Interval>> groupedIntervals = intervals.stream()
                .collect(Collectors.groupingBy(interval -> interval.getZonedDateTime().getDayOfMonth()));

        List<Float> totalTimeList = new ArrayList<>();

        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {
            List<Interval> dateIntervals = groupedIntervals.getOrDefault(currentDate.getDayOfMonth(), new ArrayList<>());
            float totalMinutes = (float) dateIntervals.stream()
                    .mapToDouble(Interval::getDurationTimeMinutes)
                    .sum();
            totalTimeList.add(totalMinutes);

            currentDate = currentDate.plusDays(1);
        }

        return totalTimeList;
    }

    @Override
    public String toString() {
        return "durationTimeMinutes=" + getDurationTimeMinutes() + ", startTime: " + startTimeMillis;
    }
}

