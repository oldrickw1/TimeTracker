package com.example.timetracker;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;
import java.util.Locale;

public class BarChartController {
    private static final int LIMIT_LINE_COLOR = Color.parseColor("#3eba00");
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final int VALUE_COLOR = Color.WHITE;
    private BarChart barChart;

    public BarChartController(BarChart barChart) {
        this.barChart = barChart;
    }

    public void setYAxis(int min, int max, float limit) {
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setTextColor(VALUE_COLOR);
        yAxis.setValueFormatter(new HourAndMinuteFormatter());
        yAxis.setLabelCount(11, true);
        yAxis.setAxisMinimum(min);
        yAxis.setAxisMaximum(max);
        yAxis.addLimitLine(createLimitLine(limit));
        barChart.getAxisRight().setEnabled(false);
    }

    private LimitLine createLimitLine(float limit) {
        LimitLine limitLine = new LimitLine(limit, "7 Hours");
        limitLine.setLineColor(LIMIT_LINE_COLOR);
        limitLine.setLineWidth(2f);
        limitLine.enableDashedLine(10f, 5f, 0f);
        limitLine.setTextColor(LIMIT_LINE_COLOR);
        return limitLine;
    }

    public void setXAxis() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(VALUE_COLOR);
        xAxis.setValueFormatter(new DayOfWeekValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
    }

    public void setData(List<BarEntry> entries, String label) {
        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setValueFormatter(new HourAndMinuteFormatter());
        barDataSet.setColor(VALUE_COLOR);
        barDataSet.setValueTextColor(VALUE_COLOR);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setBackgroundColor(BACKGROUND_COLOR);
        barChart.setDescription(null);
        barChart.setExtraOffsets(5f, 40f, 25f, 10f);
    }

    public void setLegend() {
        Legend legend = barChart.getLegend();
        legend.setTextColor(VALUE_COLOR);
        legend.setYOffset(20f);
    }

    private static class DayOfWeekValueFormatter extends ValueFormatter {
        private final String[] DAY_LABELS = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int dayIndex = (int) value -1;
            return DAY_LABELS[dayIndex];
        }
    }

    private static class HourAndMinuteFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float timeInMinutes) {
            if ((int) timeInMinutes == 0) {
                return "";
            }
            int hours = (int) (timeInMinutes / 60);
            int minutes = (int) (timeInMinutes % 60);
            return String.format(Locale.getDefault(), "%02dh:%02dm", hours, minutes);
        }
    }
}
