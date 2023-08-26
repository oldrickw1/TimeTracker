package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
    Note to self:
    This program is not loosely coupled, is not open for extension etc.
    I will finish it without any big redesigns, but later I will make a new version that can be easily changed etc.
    That way, instead of only drawing a barChart, it could also draw another chart etc. with only some minor changes.
    Also, I will build it using TDD.
 */

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = "DEBUG";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String START = "startTime";

    // View declarations
    private TextView timeTodayTV;
    private Button toGraphBtn;
    private Button startStopBtn;
    private Button deleteAllIntervalsBtn;
    private Button getIntervalsBtn;
    private Button geDailySummedIntervalsBtn;

    // State variables
    private Boolean started = false;
    private long startTime;
    private long stopTime;

    // Dependencies
    private IntervalDAO IntervalDAO;

    // Assigning Views
    private void assignViews() {
        timeTodayTV = findViewById(R.id.timeTodayTV);
        toGraphBtn = findViewById(R.id.to_graph_btn);
        startStopBtn = findViewById(R.id.start_stop_btn);
        deleteAllIntervalsBtn = findViewById(R.id.deleteAllIntervalsBtn);
        getIntervalsBtn = findViewById(R.id.getIntervalsBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntervalDAO = new IntervalDAO(this);
        assignViews();
        setupButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if (startTime == 0) {
            toggleStartStopButton(0);
        } else {
            toggleStartStopButton(1 );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (startTime != 0) {
            updateSharedPref();
        }
    }

    // Setup
    private void setupButtons() {
        setupToGraphButton();
        setupStartStopButton();
        setupDeleteIntervalsButton();
        setupGetIntervalsButton();
    }

    private void setupToGraphButton() {
        toGraphBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GraphDisplay.class)));
    }

    // For testing
    private void setupGetIntervalsButton() {
        getIntervalsBtn.setOnClickListener(v -> {
            for (Interval interval : IntervalDAO.getAllIntervals()) {
                Toast.makeText(this, "Entry: " + interval.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // For testing
    private void setupDeleteIntervalsButton() {
        deleteAllIntervalsBtn.setOnClickListener(v -> {
            IntervalDAO.deleteAllIntervals();
            Toast.makeText(this, "You've deleted all Intervals!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupStartStopButton() {
        startStopBtn.setOnClickListener(v -> {
            if (started) {
                stop();
            } else {
                start();
            }
        });
    }

    private void start() {
        startTime = System.currentTimeMillis();
        toggleStartStopButton(1);
    }

    private void stop() {
        stopTime = System.currentTimeMillis();
        IntervalDAO.addOne(startTime, stopTime);
        toggleStartStopButton(0);
        startTime = 0;
        updateSharedPref();
    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(START, startTime);
        editor.apply();
    }

    private void toggleStartStopButton(int onOffFlag) {
        if (onOffFlag == 0) {
            started = false;
            startStopBtn.setText("Start");
            startStopBtn.setBackgroundColor(Color.GREEN);
        } else if (onOffFlag == 1) {
            started = true;
            startStopBtn.setText("Stop");
            startStopBtn.setBackgroundColor(Color.RED);
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        startTime = sharedPreferences.getLong(START, 0);
    }
}
