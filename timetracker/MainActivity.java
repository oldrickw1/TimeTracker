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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = "DEBUG";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String START = "startTime";
    private static final int MILLIES_IN_SEC = 1000;

    // View declarations
    private TextView timeTodayTV;
    private Button toGraphBtn;
    private Button startStopBtn;
    private Button deleteAllEntriesBtn;
    private Button getEntriesBtn;
    private Button getEntriesBtwnBtn;

    // State variables
    private Boolean started = false;
    private long startTime;
    private long stopTime;

    // Dependencies
    private ActivityTimeLogDAO activityTimeLogDAO;

    // Assigning Views
    private void assignViews() {
        timeTodayTV = findViewById(R.id.timeTodayTV);
        toGraphBtn = findViewById(R.id.to_graph_btn);
        startStopBtn = findViewById(R.id.start_stop_btn);
        deleteAllEntriesBtn = findViewById(R.id.deleteAllEntriesBtn);
        getEntriesBtn = findViewById(R.id.getEntriesBtn);
        getEntriesBtwnBtn = findViewById(R.id.getEntriesBtwnBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityTimeLogDAO = new ActivityTimeLogDAO(this);
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
            Toast.makeText(this, "DATA SAVED", Toast.LENGTH_SHORT).show();
        }
    }

    // Setup
    private void setupButtons() {
        setupToGraphButton();
        setupStartStopButton();
        setupDeleteEntriesButton();
        setupGetEntriesButton();
        setupGetEntriesBtwnButton();
    }

    private void setupGetEntriesBtwnButton() {
        getEntriesBtwnBtn.setOnClickListener(v -> {
            List<DailyTimeSummary> dailyTimeSummaries = activityTimeLogDAO.getDailyTimesBetween(0,  System.currentTimeMillis());
            for (DailyTimeSummary day : dailyTimeSummaries) {
                Toast.makeText(this, day.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupToGraphButton() {
        toGraphBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GraphDisplay.class));
        });
    }

    private void setupGetEntriesButton() {
        getEntriesBtn.setOnClickListener(v -> {
            for (IntervalDTO entry : activityTimeLogDAO.getAllEntries()) {
                Toast.makeText(this, "Entry: " + entry.toString(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "entry: " + entry.toString());
            }
        });
    }

    private void setupDeleteEntriesButton() {
        deleteAllEntriesBtn.setOnClickListener(v -> {
            activityTimeLogDAO.deleteAllDbEntries();
            Toast.makeText(this, "You've deleted all entries!", Toast.LENGTH_SHORT).show();
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

    // Helper methods
    private void start() {
        startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * MILLIES_IN_SEC;
        toggleStartStopButton(1);
    }

    private void stop() {
        stopTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * MILLIES_IN_SEC;
        activityTimeLogDAO.addOne(startTime, stopTime);
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
        Toast.makeText(this, "StartTime: " + startTime, Toast.LENGTH_SHORT).show();
    }
}
