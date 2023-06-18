package com.example.timetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityTimeLogDAO extends SQLiteOpenHelper {

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
        Log.i("OLLIE", "addOne: delta: " + delta);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO timeSpend (timeStart, timeEnd) VALUES ('" + start + "', '" + stop + "');");
        db.close();
    }

    public void deleteAllDbEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM timeSpend;");
        db.close();
    }

    public ArrayList<ActivityEntry> getAllEntries() {
        ArrayList<ActivityEntry> activityEntries = new ArrayList<>();
        int day = 5;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM timeSpend;", null);
        while (cs.moveToNext()) {
            float time = (float)(cs.getLong(2) - cs.getLong(1)) / (float)3600;

            activityEntries.add(new ActivityEntry(time, day++, "Study"));
        }
        db.close();
        cs.close();

        return activityEntries;
    }
}
