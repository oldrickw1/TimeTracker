package com.example.timetracker;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarChartCreator {
    //todo: format the charts!
    private BarChart barChart;

    public BarChartCreator(BarChart barChart) {
        this.barChart = barChart;
    }

    public void fillBarChart(ArrayList<ActivityEntry> dummyEntries) {

        BarDataSet barDataSet1 = new BarDataSet(getDataValues1(dummyEntries), "Data Set 1");
        barDataSet1.setColor(Color.GREEN);
        BarData barData = new BarData(barDataSet1);
        barChart.setData(barData);
        barChart.setBackgroundColor(Color.LTGRAY);

        barChart.setDescription(makeDescription());
        barChart.invalidate();
    }

    private Description makeDescription() {
        Description description = new Description();
        description.setText("Hours worked this week");
        description.setTextColor(Color.BLUE);
        description.setTextSize(25);
        return description;
    }

    private ArrayList<BarEntry> getDataValues1(ArrayList<ActivityEntry> dummyEntries) {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        for (ActivityEntry entry : dummyEntries) {
            dataVals.add(new BarEntry(entry.getDay(), entry.getTime()));
        }
        return dataVals;
    }
}
