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
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button toGraphButton;
    Button startStopButton;
    Button deleteAllEntries;
    Button getAllEntries;
    Boolean started = false;
    TimeSpendDAO timeSpendDAO;
    long startTime;
    long stopTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeSpendDAO = new TimeSpendDAO(this);

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
            timeSpendDAO.deleteAllDbEntries();
        });

        getAllEntries.setOnClickListener(v -> {
            ArrayList<ActivityEntry> entries = timeSpendDAO.getAllEntries();
            for (ActivityEntry entry : entries) {
                showToast(Float.toString(entry.getTime()));
                Log.i("OLLIE", entry.toString());
            }
        });
    }

    private void stop() {
        stopTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        timeSpendDAO.addOne(startTime, stopTime);
        startStopButton.setText("Start");
        startStopButton.setBackgroundColor(Color.GREEN);
        started = false;
    }

    private void start() {
        startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        startStopButton.setText("Stop");
        startStopButton.setBackgroundColor(Color.RED);
        started = true;

    }


    private void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}