package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class GraphGenerator extends AppCompatActivity {
    private static final String RUNNING_DIFFERENCE_STRING = "Running Difference: ";
    BarChartCreator chartCreator;
    ActivityTimeLogDAO timeSpendDOA;
    BarChart mpBarChart;
    TextView weeklyTotalTV;
    TextView runningDifferenceTV;
    Button nextButton;
    Button prevButton;
    Button homeButton;
    int goal;
    int weeklyTotal;
    LocalDateTime startDate;
    final String WEEKLY_TOTAL_STRING = "Weekly Total: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_generator);

        mpBarChart = findViewById(R.id.barChart);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        weeklyTotalTV = findViewById(R.id.weeklyTotal);
        runningDifferenceTV = findViewById(R.id.runningDifferenceTV);


        chartCreator = new BarChartCreator(mpBarChart);
        timeSpendDOA = new ActivityTimeLogDAO(this);


        goal = 7;

        startDate = LocalDate.of(2023, 6, 17).atStartOfDay();



//        chartCreator.fillBarChart(timeSpendDOA.getEntriesOfThisWeekSummed(getStartOfCurrentWeek(), getEndOfCurrentWeek()));
        ArrayList<ActivityEntry> entries = timeSpendDOA.getDummyDataWeekEntries();
        chartCreator.fillBarChart(entries);
        updateWeeklyTotalText(entries);
        updateDifference(getRunningDifference(null));



        prevButton.setOnClickListener(updateChart);
        nextButton.setOnClickListener(updateChart);

        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    private void updateDifference(String runningDifference) {
        runningDifferenceTV.setText(RUNNING_DIFFERENCE_STRING + runningDifference);
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

    private View.OnClickListener updateChart = v -> {
         ArrayList<ActivityEntry> entries = null;
        if (v.getId() == R.id.prevButton) {
              entries = timeSpendDOA.getEntriesOfBetweenSummed(getStartOfCurrentWeek(), getEndOfCurrentWeek());
            Toast.makeText(this, entries.toString(), Toast.LENGTH_SHORT).show();
            chartCreator.fillBarChart(entries);
//        } else if (v.getId() == R.id.nextButton) {
//            entries = timeSpendDOA.getNextWeek()
//            chartCreator.fillBarChart(entries);
        updateWeeklyTotalText(entries);
        }
    };

    private String getRunningDifference(LocalDateTime endDate) {
        long totalMinutes;
        long totalGoalTime;
        if (!Objects.isNull(endDate)) {
            totalMinutes = getTotalMinutes(timeSpendDOA.getEntriesOfBetween(startDate.toEpochSecond(ZoneOffset.UTC), endDate.toEpochSecond(ZoneOffset.UTC)));
            totalGoalTime = Duration.between(startDate, endDate).toDays() * goal * timeSpendDOA.MINUTES_IN_HOUR;
        } else {
            totalMinutes = getTotalMinutes(timeSpendDOA.getAllEntries());
            totalGoalTime = Duration.between(startDate, LocalDateTime.now()).toDays() * goal * timeSpendDOA.MINUTES_IN_HOUR;
        }
        long difference = totalMinutes - totalGoalTime;
        return toHoursAndMinute(difference);
    }

    private long getTotalMinutes(ArrayList<ActivityEntry> allEntries) {
        long totMins = 0;
        for (ActivityEntry entry : allEntries) {
            totMins += entry.getTime();
        }
        return totMins;
    }

    private void updateWeeklyTotalText(ArrayList<ActivityEntry> entries) {
        weeklyTotalTV.setText(WEEKLY_TOTAL_STRING + getWeeklyTotal(entries));
    }

    private String getWeeklyTotal(ArrayList<ActivityEntry> entries) {
        int tot = 0;
        for (ActivityEntry entry : entries) {
            tot += entry.getTime();
        }
        return toHoursAndMinute(tot);
    }

    private String toHoursAndMinute(long tot) {
        return (int) (tot / 60) + ":" + (int) (tot % 60) + "h";
    }

}