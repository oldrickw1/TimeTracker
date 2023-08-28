package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class GraphDisplay extends AppCompatActivity {
    // Constants
    private static final String RUNNING_DIFFERENCE_STRING = "Running Difference: ";
    private static final String TAG = "DEBUG";
    private static final int WEEKLY_GOAL = 5 * 7 + 2 * 4;

    // Dependencies
    private BarChartController chartController;
    private IntervalDAO intervalDAO;

    // Views
    private BarChart barChart;
    private TextView weeklyTotalTV;
    private TextView deltaTV;
    private TextView graphDateTV;
    private Button nextButton;
    private Button prevButton;
    private Button homeButton;

    // State variables
    private long currentWeekStart;
    private long currentWeekEnd;


    private void assignViews() {
        barChart = findViewById(R.id.barChart);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        weeklyTotalTV = findViewById(R.id.weeklyTotal);
        deltaTV = findViewById(R.id.deltaTV);
        graphDateTV = findViewById(R.id.graphDate);
    }

    private void setButtons() {
        setupHomeButton();
        setPreviousWeekButton();
        setNextWeekButton();
    }

    private void setPreviousWeekButton() {
        prevButton.setOnClickListener((view -> {
            currentWeekStart -= 7 * 24 * 60 * 60 * 1000; // Subtract 7 days in milliseconds
            currentWeekEnd -= 7 * 24 * 60 * 60 * 1000;
            setGraphAndDescriptors(currentWeekStart, currentWeekEnd);
        }));
    }
    private void setNextWeekButton() {
        nextButton.setOnClickListener((view -> {
            currentWeekStart += 7 * 24 * 60 * 60 * 1000; // Subtract 7 days in milliseconds
            currentWeekEnd += 7 * 24 * 60 * 60 * 1000;
            setGraphAndDescriptors(currentWeekStart, currentWeekEnd);
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_display);

        currentWeekStart = getStartOfCurrentWeek();
        currentWeekEnd = getEndOfCurrentWeek();

        assignViews();
        chartController = new BarChartController(barChart);
        intervalDAO = new IntervalDAO(this);
        setButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGraphAndDescriptors(currentWeekStart, currentWeekEnd);
    }

    private void setGraphAndDescriptors(long start, long end) {
        List<Interval> intervals = intervalDAO.getAllIntervalsBetween(start, end);
        intervals.forEach(interval -> {
            System.out.println("setGraphAndDescriptors: date: " + interval.getZonedDateTime().format(DateTimeFormatter.ofPattern("dd-MMMM-yy")));
        });
        long totalTimeInMillis = Interval.getTotalTimeFromListOfIntervals(intervals);
        setWeeklyTotal(totalTimeInMillis);
        setDelta(totalTimeInMillis);
        List<Float> totalMinutesByDate = Interval.calculateTotalMinutesByDate(millisToLocalDate(start), millisToLocalDate(end), intervals);
        setBarChart(totalMinutesByDate);
        setGraphDate(LocalDate.ofEpochDay(currentWeekStart / (24 * 60 * 60 * 1000)), LocalDate.ofEpochDay(currentWeekEnd / (24 * 60 * 60 * 1000)));
    }

    private void setDelta(long actualTimeMillis) {
        long goalMillis = (long) WEEKLY_GOAL * 60 * 60 * 1000;
        long deltaMillis = goalMillis - actualTimeMillis;
        String deltaDisplay = deltaMillis <= 0 ? "Surplus of : " : "Deficit of ";
        deltaTV.setText(deltaDisplay + Interval.getHoursAndMinutes(Math.abs(deltaMillis)));
    }

    private void setGraphDate(LocalDate beginDate, LocalDate endDate) {
        String beginDayOfMonth = String.valueOf(beginDate.getDayOfMonth());
        String beginMonth = String.valueOf(beginDate.getMonth());
        String endDayOfMonth = String.valueOf(endDate.getDayOfMonth());
        String endMonth = String.valueOf(endDate.getMonth());
        String text = ((beginMonth.equals(endMonth)) ? beginDayOfMonth + " - " + endDayOfMonth + " " + endMonth : beginDayOfMonth + " " + beginMonth + " - " + endDayOfMonth + " " + endMonth) + " "  + endDate.getYear();
        graphDateTV.setText(text);
    }

    private void setWeeklyTotal(long totalTimeMillis) {
        weeklyTotalTV.setText("Total: " + Interval.getHoursAndMinutes(totalTimeMillis));
    }

    private void setBarChart(List<Float> totalMinutesGrouped) {
        List<BarEntry> entries = adapt(totalMinutesGrouped);
        chartController.setData(entries, "Time Studied");
        chartController.setYAxis(0, 600, 420);
        chartController.setXAxis();
        chartController.setLegend();
        barChart.invalidate();
    }

    private long getEndOfCurrentWeek() {
        LocalDate currentDate = LocalDate.now();
        LocalDate endOfWeek = currentDate.with(DayOfWeek.SUNDAY);
        ZonedDateTime endOfWeekZonedDateTime = endOfWeek.atTime(23, 59, 59).atZone(ZoneId.systemDefault());
        return endOfWeekZonedDateTime.toInstant().toEpochMilli();
    }

    private long getStartOfCurrentWeek() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        ZonedDateTime startOfWeekZonedDateTime = startOfWeek.atStartOfDay(ZoneId.systemDefault());
        return startOfWeekZonedDateTime.toInstant().toEpochMilli();
    }

    private List<BarEntry> adapt(List<Float> totalMinutesGroupedByDayOfWeek) {
        List<BarEntry> barEntries = new ArrayList<>();
        for (float minutes : totalMinutesGroupedByDayOfWeek) {
            System.out.println("adapt: minutes: " + minutes);
        }
        for (int i = 0; i < 7; i++) {
            float hoursAndMinutes = totalMinutesGroupedByDayOfWeek.get(i);
            barEntries.add(new BarEntry(i, hoursAndMinutes));
        }

        return barEntries;
    }

    private void setupHomeButton() {
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    public LocalDate millisToLocalDate(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        LocalDate localDate = zonedDateTime.toLocalDate();
        return localDate;
    }

}
