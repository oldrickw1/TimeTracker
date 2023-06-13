package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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
    BarChartCreator chartCreator;
    TimeSpendDOA timeSpendDOA;
    BarChart mpBarChart;
    Button nextButton;
    Button prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_generator);


        mpBarChart = findViewById(R.id.barChart);
        chartCreator = new BarChartCreator(mpBarChart);
        timeSpendDOA = new TimeSpendDOA();

        chartCreator.fillBarChart(timeSpendDOA.getDummyEntries());


    }
}