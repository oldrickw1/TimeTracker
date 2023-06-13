package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button toGraphButton;
    Button startStopButton;
    Boolean started = false;
    TimeSpendDAO timeSpendDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeSpendDAO = new TimeSpendDAO(this);

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
        startStopButton.setText("Start");
        startStopButton.setBackgroundColor(Color.GREEN);
        started = false;
    }

    private void start() {
        timeSpendDAO.addOne(new ActivityEntry(1,1,null));
        startStopButton.setText("Stop");
        startStopButton.setBackgroundColor(Color.RED);
        started = true;

    }
}