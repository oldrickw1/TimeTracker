package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GraphDisplay extends AppCompatActivity {
    private static final String RUNNING_DIFFERENCE_STRING = "Running Difference: ";
    private static final String TAG = "DEBUG";

    private BarChartController chartController;
    private IntervalDAO intervalDAO;
    private BarChart barChart;
    private TextView weeklyTotalTV;
    private TextView runningDifferenceTV;
    private TextView graphDateTV;
    private Button nextButton;
    private Button prevButton;
    private Button homeButton;

    private void assignViews() {
        barChart = findViewById(R.id.barChart);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        weeklyTotalTV = findViewById(R.id.weeklyTotal);
        runningDifferenceTV = findViewById(R.id.runningDifferenceTV);
        graphDateTV = findViewById(R.id.graphDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_display);

        assignViews();
        chartController = new BarChartController(barChart);
        intervalDAO = new IntervalDAO(this);
        setupHomeButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBarChart();
    }

    private void setBarChart() {
        List<Interval> intervals = intervalDAO.getAllIntervalsBetween(getStartOfCurrentWeek(), getEndOfCurrentWeek());
        Map<LocalDate, Double> totalTimeByDate = Interval.groupAndCalculateTotalTimeByDate(intervals);
        totalTimeByDate.forEach(((localDate, aDouble) -> Log.i(TAG, "setBarChart: date: " + localDate.getDayOfWeek() + " totalTime: " + aDouble)));
        List<BarEntry> entries = adapt(totalTimeByDate);
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

    private List<BarEntry> adapt(Map<LocalDate, Double> totalTimeByDate) {
        List<BarEntry> barEntries = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalDate date = LocalDate.now().with(dayOfWeek);
            Double totalTime = totalTimeByDate.getOrDefault(date, 0.0);

            // Convert dayOfWeek to float for x and totalTime to float for y
            barEntries.add(new BarEntry(dayOfWeek.getValue(), totalTime.floatValue()));
        }

        return barEntries;
    }

    private void setupHomeButton() {
        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
