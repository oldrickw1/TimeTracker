package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button toGraphButton;
    Button startStopButton;
    Boolean started = false;
    TimeSpendDAO timeSpendDAO;
    Date date;
    private Date startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeSpendDAO = new TimeSpendDAO(this);

        date = new Date();

        toGraphButton = findViewById(R.id.goToOtherActivity);
        startStopButton = findViewById(R.id.startStop);

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
    }

    private void stop() {
        timeSpendDAO.addOne(startTime);
        startStopButton.setText("Start");
        startStopButton.setBackgroundColor(Color.GREEN);
        started = false;
    }

    private void start() {
        startTime = new Date(date.getTime());
        startStopButton.setText("Stop");
        startStopButton.setBackgroundColor(Color.RED);
        started = true;

    }
}