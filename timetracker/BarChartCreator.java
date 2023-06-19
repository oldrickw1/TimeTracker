package com.example.timetracker;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BarChartCreator {
    //todo: format the charts!
    private BarChart barChart;

    public BarChartCreator(BarChart barChart) {
        this.barChart = barChart;
    }

    public void fillBarChart(ArrayList<ActivityEntry> entries) {
        LimitLine limitLine = new LimitLine(7f, "Goal");
        limitLine.setLineColor(Color.GREEN);
        limitLine.setLineWidth(2f);
        limitLine.enableDashedLine(10f, 5f, 0f);
        limitLine.setTextColor(Color.GREEN);


        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(11);
        yAxis.addLimitLine(limitLine);


        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisLabels(entries)));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        BarDataSet barDataSet1 = new BarDataSet(extractDataValues(entries), "Hours Studied");
        barDataSet1.setColor(Color.WHITE);

        BarData barData = new BarData(barDataSet1);


        barChart.setData(barData);
        barChart.setBackgroundColor(Color.LTGRAY);

        barChart.setDescription(null);
        barChart.invalidate();
    }

    private Description makeDescription(String text, int size) {
        Description description = new Description();
        description.setText(text);
        description.setYOffset(-27);
        description.setTextColor(Color.DKGRAY);
        description.setTextSize(size);
        return description;
    }

    private ArrayList<BarEntry> extractDataValues(ArrayList<ActivityEntry> entries) {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        int i = 0;
        for (ActivityEntry entry : entries) {
            dataVals.add(new BarEntry(i++, (entry.getHoursAndMinutes())));
        }
        return dataVals;
    }

    private ArrayList<String> getXAxisLabels(ArrayList<ActivityEntry> entries) {
        ArrayList<String> labels = new ArrayList<>();
        for (ActivityEntry entry : entries) {
            labels.add(entry.getDate().format(DateTimeFormatter.ofPattern("EEE")));
        }
        return labels;
    }
}
