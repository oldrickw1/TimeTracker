package com.example.timetracker;

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
    final int SECONDS_IN_MINUTE = 60;
    final int MINUTES_IN_HOUR = 60;
    final int MILLIESECONDS_IN_SECOND = 1000;

    public ActivityTimeLogDAO(Context context) {
        super(context, "activity_time_log.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE activity_time_log (id INTEGER PRIMARY KEY AUTOINCREMENT , timeStart INTEGER, timeEnd INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOne(long start, long stop) {
        long delta = stop - start;
        Log.i("OLLIE", "addOne: start: " + start + ", stop: " + stop + " delta: " + delta + ", stop EPOCH time: " + stop);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO activity_time_log (timeStart, timeEnd) VALUES ('" + start + "', '" + stop + "');");
        db.close();
    }

    public void deleteAllDbEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM activity_time_log;");
        db.close();
    }

    public ArrayList<ActivityEntry> getAllEntries() {
        ArrayList<ActivityEntry> activityEntries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM activity_time_log;", null);
        while (cs.moveToNext()) {
            float time = (float) (cs.getLong(2) - cs.getLong(1)) / (float) (SECONDS_IN_MINUTE * MINUTES_IN_HOUR * MILLIESECONDS_IN_SECOND);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(cs.getLong(2)), ZoneId.systemDefault());
            activityEntries.add(new ActivityEntry(time, zonedDateTime, "Study"));
        }
        db.close();
        cs.close();

        return activityEntries;
    }

    public ArrayList<ActivityEntry> getEntriesOfBetween(long minDate, long maxDate) {
        ArrayList<ActivityEntry> activityEntries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM activity_time_log WHERE timeEnd BETWEEN " + minDate + " AND " + maxDate + " ORDER BY timeEnd ASC;", null);
        while (cs.moveToNext()) {
            float time = (float) (cs.getLong(2) - cs.getLong(1)) / (float) (SECONDS_IN_MINUTE * MINUTES_IN_HOUR * MILLIESECONDS_IN_SECOND);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(cs.getLong(2)), ZoneId.systemDefault());
            activityEntries.add(new ActivityEntry(time, zonedDateTime, "Study"));
        }
        db.close();
        cs.close();

        return activityEntries;
    }

    public ArrayList<ActivityEntry> getEntriesOfBetweenSummed(long minDate, long maxDate) {
        ArrayList<ActivityEntry> results = new ArrayList<>();
        ArrayList<ActivityEntry> allEntries = getEntriesOfBetween(minDate, maxDate);
        if (allEntries.size() == 0) {
            return allEntries;
        }
        ZonedDateTime firstDay = allEntries.get(0).getDate();
        float timeSpend = 0;
        for (ActivityEntry entry : allEntries) {
            Log.i("Oldrick", "getEntriesOfThisWeekSummed: all entries: " + entry);
            if (entry.getDate().getDayOfMonth() == firstDay.getDayOfMonth()) {
                timeSpend += entry.getTime();
            } else {
                results.add(new ActivityEntry(timeSpend / SECONDS_IN_MINUTE, firstDay, "Study"));
                timeSpend = 0;
                firstDay = entry.getDate();
            }
        }
        if (timeSpend != 0) {
            results.add(new ActivityEntry(timeSpend / SECONDS_IN_MINUTE, firstDay, "Study"));
        }
        Log.i("Oldrick", "getEntriesOfThisWeekSummed: results: " + results);
        return results;
    }

    public ArrayList<ActivityEntry> getDummyDataWeekEntries() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<ActivityEntry> data = new ArrayList<>(List.of(
                new ActivityEntry(240, LocalDate.parse("12/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(440, LocalDate.parse("13/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(430, LocalDate.parse("14/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(340, LocalDate.parse("15/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(220, LocalDate.parse("16/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(540, LocalDate.parse("17/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study"),
                new ActivityEntry(340, LocalDate.parse("18/06/2023", formatter).atStartOfDay(ZoneId.systemDefault()), "Study")
        ));
        return data;
    }

}
