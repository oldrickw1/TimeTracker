package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;


public class GraphGenerator extends AppCompatActivity {
    BarChartCreator chartCreator;
    TimeSpendDAO timeSpendDOA;
    BarChart mpBarChart;
    TextView weeklyTotalTV;
    Button nextButton;
    Button prevButton;
    Button homeButton;
    int weeklyTotal;
    String weeklyTotalString = "Weekly Total: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_generator);

        mpBarChart = findViewById(R.id.barChart);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);
        weeklyTotalTV = findViewById(R.id.weeklyTotal);

        chartCreator = new BarChartCreator(mpBarChart);
        timeSpendDOA = new TimeSpendDAO(this);

        chartCreator.fillBarChart(timeSpendDOA.getDummyEntries());
        updateWeeklyTotalText(); //todo: ask on r/learnprogramming how to deal with the separation of data queries (weekly total and the update data from the chartCreator)

        prevButton.setOnClickListener(updateChart);
        nextButton.setOnClickListener(updateChart);

        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    private View.OnClickListener updateChart = v -> {
        if (v.getId() == R.id.prevButton) {
            chartCreator.fillBarChart(timeSpendDOA.getPrevWeek());
        } else if (v.getId() == R.id.nextButton) {
            chartCreator.fillBarChart(timeSpendDOA.getNextWeek());
        }
        updateWeeklyTotalText();
    };

    private void updateWeeklyTotalText() {
        weeklyTotalTV.setText(weeklyTotalString + weeklyTotal);
    }

}