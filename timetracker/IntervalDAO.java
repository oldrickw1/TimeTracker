package com.example.timetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class IntervalDAO extends SQLiteOpenHelper {
    private final String TABLE_NAME = "timeTable";
    private final String START_TIME_MILLIS = "timeStartInEpochMillis";
    private final String END_TIME_MILLIS = "timeEndInEpochMillis";

    public IntervalDAO(Context context) {
        super(context, "activity_time_log.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " + START_TIME_MILLIS + " INTEGER, " + END_TIME_MILLIS + " INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void addOne(long start, long stop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + START_TIME_MILLIS + "," + END_TIME_MILLIS + " ) VALUES ('" + start + "', '" + stop + "');");
        db.close();
    }

    public void deleteAllIntervals() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
        db.close();
    }


    public List<Interval> getAllIntervalsBetween(long minDateInMillis, long maxDateInMillis) {
        List<Interval> intervals = new ArrayList<>();

        String query = "SELECT "
                + START_TIME_MILLIS + ", "
                + END_TIME_MILLIS + " "
                + "FROM " + TABLE_NAME + " "
                + "WHERE " + START_TIME_MILLIS + " >= " + minDateInMillis + " "
                + "AND " + END_TIME_MILLIS + " <= " + maxDateInMillis + " "
                + "ORDER BY " + END_TIME_MILLIS + ";";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery(query, null);
        while (cs.moveToNext()) {
            intervals.add(extractDataFromCursor(cs));
        }
        cs.close();
        db.close();
        return intervals;
    }

    public List<Interval> getAllIntervals() {
        return getAllIntervalsBetween(0, System.currentTimeMillis());
    }

    private Interval extractDataFromCursor(Cursor cs) {
        long startTimeMillis = cs.getLong(cs.getColumnIndexOrThrow(START_TIME_MILLIS));
        long endTimeMillis = cs.getLong(cs.getColumnIndexOrThrow(END_TIME_MILLIS));
        return new Interval(startTimeMillis, endTimeMillis, "Study");
    }
}

