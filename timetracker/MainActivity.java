package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button toGraphButton;
    Button startStopButton;
    Button deleteAllEntries;
    Button getAllEntries;
    Boolean started = false;
    ActivityTimeLogDAO activityTimeLogDAO;
    long startTime;
    long stopTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityTimeLogDAO = new ActivityTimeLogDAO(this);

        toGraphButton = findViewById(R.id.goToOtherActivity);
        startStopButton = findViewById(R.id.startStop);
        deleteAllEntries = findViewById(R.id.deleteAllEntries);
        getAllEntries = findViewById(R.id.getEntries);

        toGraphButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GraphGenerator.class);
            startActivity(intent);
        });

        startStopButton.setOnClickListener(v -> {
            if (started) {
                stop();
            } else {
                start();
            }
        });

        deleteAllEntries.setOnClickListener(v -> {
            showToast("!!! DELETING ALL ENTRIES !!!");
            activityTimeLogDAO.deleteAllDbEntries();
        });

        getAllEntries.setOnClickListener(v -> {
            ArrayList<ActivityEntry> entries = activityTimeLogDAO.getAllEntries();
            for (ActivityEntry entry : entries) {
                showToast(Float.toString(entry.getTime()));
                Log.i("OLLIE", entry.toString());
            }
        });
    }

    private void stop() {
        stopTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        activityTimeLogDAO.addOne(startTime, stopTime);
        toggleStartStopButton();
        started = false;
    }

    private void start() {
        startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        toggleStartStopButton();
        started = true;

    }

    private void toggleStartStopButton() {
        if (started) {
            startStopButton.setText("Stop");
            startStopButton.setBackgroundColor(Color.RED);
        } else {
            startStopButton.setText("Start");
            startStopButton.setBackgroundColor(Color.GREEN);
        }
    }


    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}