package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;


public class GraphGenerator extends AppCompatActivity {
    BarChart mpBarChart;

    private final ArrayList<ActivityEntry> dummyEntries = new ArrayList<>(
            Arrays.asList(
                    new ActivityEntry(4, 0,"Programming"),
                    new ActivityEntry(5, 1, "Programming"),
                    new ActivityEntry(3, 2, "Programming"),
                    new ActivityEntry(5, 3, "Programming"),
                    new ActivityEntry(7, 4, "Programming")
            )
    );




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_generator);

        mpBarChart = findViewById(R.id.barChart);
        BarDataSet barDataSet1 = new BarDataSet(getDataValues1(), "Data Set 1");
        barDataSet1.setBarBorderColor(Color.GREEN);

        BarData barData = new BarData(barDataSet1);

        mpBarChart.setData(barData);
        mpBarChart.invalidate();

    }

    private ArrayList<BarEntry> getDataValues1() {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        for (ActivityEntry entry : dummyEntries) {
            dataVals.add(new BarEntry(entry.getDay(), entry.getTime()));
        }
        return dataVals;
    }
}