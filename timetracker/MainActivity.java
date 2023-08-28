package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/*
    Note to self:
    This program is not loosely coupled, is not open for extension etc.
    I will finish it without any big redesigns, but later I will make a new version that can be easily changed etc.
    That way, instead of only drawing a barChart, it could also draw another chart etc. with only some minor changes.
    Also, I will build it using TDD.

    I discovered Java has an in-build Duration class. Consider using it in your Interval class in next refactor.
    Consider using more sql functionality to group data etc.

 */

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = "DEBUG";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String START = "startTime";

    // View declarations
    private TextView runningDurationTV;
    private Button toGraphBtn;
    private Button startStopBtn;
    private Button deleteAllIntervalsBtn;
    private Button getIntervalsBtn;
    private Button insertDummyDataBtn;

    // State variables
    private Boolean started = false;
    private long startTime;
    private long stopTime;

    // Dependencies
    private IntervalDAO intervalDAO;
    private Timer timer = new Timer();

    // Assigning Views
    private void assignViews() {
        runningDurationTV = findViewById(R.id.runningDurationTV);
        toGraphBtn = findViewById(R.id.to_graph_btn);
        startStopBtn = findViewById(R.id.start_stop_btn);
        deleteAllIntervalsBtn = findViewById(R.id.deleteAllIntervalsBtn);
        getIntervalsBtn = findViewById(R.id.getIntervalsBtn);
        insertDummyDataBtn = findViewById(R.id.insertDummyDataBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intervalDAO = new IntervalDAO(this);
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
            startTimer();
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
        setupInsertDummyDataButton();
    }

    private void setupInsertDummyDataButton() {
        insertDummyDataBtn.setOnClickListener(view -> {
            insertDummyData();
            insertDummyDataBtn.setEnabled(false);
        });
    }

    private void setupToGraphButton() {
        toGraphBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GraphDisplay.class)));
    }

    // For testing
    private void setupGetIntervalsButton() {
        getIntervalsBtn.setOnClickListener(v -> {
            for (Interval interval : intervalDAO.getAllIntervals()) {
                System.out.println("MainActivity: Interval: Start: " + interval.getStartTimeMillis() + ", End: " + interval.getEndTimeMillis());
            }
        });
    }

    // For testing
    private void setupDeleteIntervalsButton() {
        deleteAllIntervalsBtn.setOnClickListener(v -> {
            intervalDAO.deleteAllIntervals();
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
        startTimer();
    };

    private void stop() {
        stopTime = System.currentTimeMillis();
        intervalDAO.addOne(startTime, stopTime);
        toggleStartStopButton(0);
        startTime = 0;
        updateSharedPref();
        stopTimer();
        runningDurationTV.setText("Time spent: ");
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

    private void scheduleTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    runningDurationTV.setText("Time spent: " + millisToTime(System.currentTimeMillis() - startTime));
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        scheduleTask();
    }

    private String millisToTime(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) (millis / (1000 * 60 * 60));

        return String.format(Locale.getDefault(), "%02dh:%02dm:%02ds", hours, minutes, seconds);
    }

    // METHOD TO INSERT DUMMY DATA
    /*
    21/08/24 9:30 - 21/08/24 17:00

Start: 1679758200000
End: 1679782800000
22/08/24 9:43 - 22/08/24 17:00

Start: 1679844180000
End: 1679869200000
23/08/24 10:23 - 23/08/24 17:00

Start: 1679931780000
End: 1679955600000
24/08/24 8:04 - 24/08/24 17:00

Start: 1680007440000
End: 1680032400000
25/08/24 11:33 - 25/08/24 17:00

Start: 1680099180000
End: 1680121200000
26/08/24 12:40 - 26/08/24 17:00

Start: 1680190800000
End: 1680202800000
     */
    private void insertDummyData() {
        System.out.println("inserting dummy data!");
        long dummyIntervals[][] = {
                {1693206000000L, 1693234900000L},
                {1693292400000L, 1693324400000L},
                {1693378800000L, 1693393500000L},
                {1693461600000L, 1693486400000L},
                {1693551600000L, 1693584100000L},
                {1693638000000L, 1693670900000L},
        };
        for (long[] interval : dummyIntervals) {
            intervalDAO.addOne(interval[0], interval[1]);
        }
    }
}
