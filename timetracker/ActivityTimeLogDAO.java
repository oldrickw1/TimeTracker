package com.example.timetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityTimeLogDAO extends SQLiteOpenHelper {
    public final int SECONDS_IN_MINUTE = 60;
    public final int MINUTES_IN_HOUR = 60;
    public final int MILLIESECONDS_IN_SECOND = 1000;
    private final String TABLE_NAME = "timeTable";
    private final String TIME_START_COLUMN = "timeStart";
    private final String TIME_END_COLUMN = "timeEnd";

    public ActivityTimeLogDAO(Context context) {
        super(context, "activity_time_log.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " + TIME_START_COLUMN + " INTEGER, " + TIME_END_COLUMN + " INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void addOne(long start, long stop) {
        long delta = stop - start;
        Log.i("OLLIE", "addOne: start: " + start + ", stop: " + stop + " delta: " + delta + ", stop EPOCH time: " + stop);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO timeTable (timeStart, timeEnd) VALUES ('" + start + "', '" + stop + "');");
        db.close();
    }

    public void deleteAllDbEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM timeTable;");
        db.close();
    }

    public ArrayList<IntervalDTO> getAllEntries() {
        return getEntriesFromQuery("SELECT * FROM " + TABLE_NAME + ";");
    }

    public ArrayList<IntervalDTO> getEntriesOfBetween(long minDate, long maxDate) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TIME_START_COLUMN + " BETWEEN " + minDate + " AND " + maxDate + " ORDER BY timeEnd ASC;";
        return getEntriesFromQuery(query);
    }

    private ArrayList<IntervalDTO> getEntriesFromQuery(String query) {
        ArrayList<IntervalDTO> activityEntries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery(query, null);
        while (cs.moveToNext()) {
            activityEntries.add(extractEntryFromCursor(cs));
        }
        db.close();
        cs.close();
        return activityEntries;
    }

    private IntervalDTO extractEntryFromCursor(Cursor cs) {
        float millies = cs.getLong(2) - cs.getLong(1);
        float minutes = convertMilliesToMinutes(millies);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(cs.getLong(2)), ZoneId.systemDefault()); // By getting the TIME_END_COLUMN, the choice is made to count the interval to that day in which the activity ends. If you do the activity from 23:20 to 2:22, it belongs to the next day.
        return new IntervalDTO(minutes, zonedDateTime, "Study");
    }

    private float convertMilliesToMinutes(float intervalInMillies) {
        return intervalInMillies / (float) (SECONDS_IN_MINUTE * MILLIESECONDS_IN_SECOND);
    }

    public ArrayList<IntervalDTO> getDailySummedIntervals(long minDate, long maxDate) {
        ArrayList<IntervalDTO> dailyTimesSpend = new ArrayList<>();
        ArrayList<IntervalDTO> allEntries = getEntriesOfBetween(minDate, maxDate);
        if (allEntries.size() == 0) {
            return allEntries;
        }
        ZonedDateTime lastProcessedDate = allEntries.get(0).getDate();
        float timeSpend = 0;
        for (IntervalDTO entry : allEntries) {
            if (entry.getDate().getDayOfMonth() == lastProcessedDate.getDayOfMonth()) {
                timeSpend += entry.getMinutes(); // accumulating the time spent for the same day
            } else {
                dailyTimesSpend.add(new IntervalDTO(timeSpend, lastProcessedDate, "Study"));
                timeSpend = 0;
                lastProcessedDate = entry.getDate();
            }
        }
        if (timeSpend != 0) { // adding last day
            dailyTimesSpend.add(new IntervalDTO(timeSpend, lastProcessedDate, "Study"));
        }
        return dailyTimesSpend;
    }

    public ArrayList<IntervalDTO> getDummyDataWeekEntries() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<IntervalDTO> data = new ArrayList<>(List.of(
                new IntervalDTO(240, LocalDate.parse("12/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(440, LocalDate.parse("13/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(430, LocalDate.parse("14/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(340, LocalDate.parse("15/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(220, LocalDate.parse("16/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(540, LocalDate.parse("17/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new IntervalDTO(340, LocalDate.parse("18/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study")
        ));
        return data;
    }

    public List<DailyTimeSummary> getDailyTimesBetween(long minDate, long maxDate) {
        ArrayList<DailyTimeSummary> dailyTimeSummaries = new ArrayList<>();

        String query = "SELECT strftime('%Y-%m-%d', timeStart / 1000, 'unixepoch') AS day, SUM(timeEnd - timeStart) AS totalSeconds FROM timeTable WHERE timeStart >= " + minDate + " AND timeEnd <= " + maxDate + " GROUP BY day ORDER BY day;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery(query, null);
        while (cs.moveToNext()) {
            @SuppressLint("Range") String date = cs.getString(cs.getColumnIndex("day"));
            long totalMillieSeconds = cs.getLong(1);
            dailyTimeSummaries.add(new DailyTimeSummary(date, totalMillieSeconds));
        }
        db.close();
        return dailyTimeSummaries;
    }
}
