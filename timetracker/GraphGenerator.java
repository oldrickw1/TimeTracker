package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;



public class GraphGenerator extends AppCompatActivity {
    BarChart mpBarChart;

    private final

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_generator);

        mpBarChart = findViewById(R.id.barChart);
        LineDataSet lineDataSet = new LineDataSet(getDataValues(), "Data Set 1");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);

    }

    private ArrayList<BarEntry> getDataValues() {
        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0, 20));
        dataVals.add(new Entry(1, 26));
        dataVals.add(new Entry(2, 22));
        dataVals.add(new Entry(3, 2));
        dataVals.add(new Entry(4, 10));

        return dataVals;
    }
}